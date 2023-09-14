package ru.researchser.user.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.researchser.mailSender.MailSenderService;
import ru.researchser.mailSender.SendMailRequest;
import ru.researchser.user.models.enums.ERole;
import ru.researchser.user.models.Role;
import ru.researchser.user.models.User;
import ru.researchser.user.models.enums.UserStatus;
import ru.researchser.user.security.payloads.request.LoginRequest;
import ru.researchser.user.security.payloads.request.SignupRequest;
import ru.researchser.user.security.payloads.response.JwtResponse;
import ru.researchser.user.security.payloads.response.MessageResponse;
import ru.researchser.user.models.UserDetailsImpl;
import ru.researchser.user.repositories.RoleRepository;
import ru.researchser.user.repositories.UserRepository;
import ru.researchser.utils.CryptoUtil;
import ru.researchser.utils.JwtUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final MailSenderService senderService;
    private final CryptoUtil cryptoUtil;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setUserStatus(UserStatus.WAIT_FOR_EMAIL_VERIFICATION);

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        String cryptoUserId = cryptoUtil.hashOf(user.getId());

        SendMailRequest sendMailRequest = new SendMailRequest()
                .builder()
                .cryptoUserId(cryptoUserId)
                .userEmail(user.getEmail())
                .build();

        senderService.sendVerificationEmail(sendMailRequest);
        return ResponseEntity.ok(
                new MessageResponse("User registered successfully and waits for email verification!"));
    }

    @GetMapping("/activation?id={id}")
    public ResponseEntity<?> activateUser(@RequestParam("id") String cryptoUserId) {
        Long userId = cryptoUtil.idOf(cryptoUserId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();
            if (user.getUserStatus().equals(UserStatus.WAIT_FOR_EMAIL_VERIFICATION)) {
                user.setUserStatus(UserStatus.CONFIRMED_ACCOUNT);
                return ResponseEntity
                        .ok(new MessageResponse("Account confirmed successfully"));
            } else if (user.getUserStatus().equals(UserStatus.CONFIRMED_ACCOUNT)) {
                log.debug("Account is already confirmed!");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Account is already confirmed!"));
            }
        }
        log.debug("User with user id not found. Link isn't valid");
        return ResponseEntity
                .unprocessableEntity()
                .body(new MessageResponse("The link isn't valid"));
    }

}
