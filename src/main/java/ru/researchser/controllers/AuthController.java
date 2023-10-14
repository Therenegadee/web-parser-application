package ru.researchser.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.researchser.services.mailSender.MailSenderService;
import ru.researchser.services.mailSender.SendMailRequest;
import ru.researchser.openapi.api.AuthApiDelegate;
import ru.researchser.openapi.model.*;
import ru.researchser.services.security.JwtUtils;
import ru.researchser.utils.CryptoUtil;
import ru.researchser.models.user.Role;
import ru.researchser.models.user.User;
import ru.researchser.models.user.enums.ERole;
import ru.researchser.repositories.RoleRepository;
import ru.researchser.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Log4j
public class AuthController implements AuthApiDelegate {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final MailSenderService senderService;
    private final CryptoUtil cryptoUtil;

    @Override
    @PostMapping("/signin")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(@Valid @RequestBody LoginRequestOpenApi loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            User user = (User) authentication.getPrincipal();
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity
                    .ok(new JwtResponseOpenApi(jwt,
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            roles));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PostMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignupRequestOpenApi signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setUserStatus(UserStatusOpenApi.WAIT_FOR_EMAIL_VERIFICATION);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        String cryptoUserId = cryptoUtil.hashOf(user.getId());
        SendMailRequest sendMailRequest = new SendMailRequest()
                .builder()
                .cryptoUserId(cryptoUserId)
                .userEmail(user.getEmail())
                .build();
        senderService.sendVerificationEmail(sendMailRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    @PatchMapping("/activation")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> activateUser(@RequestParam("id") String cryptoUserId) {
        Long userId = cryptoUtil.idOf(cryptoUserId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();
            if (user.getUserStatus().equals(UserStatusOpenApi.WAIT_FOR_EMAIL_VERIFICATION)) {
                user.setUserStatus(UserStatusOpenApi.CONFIRMED_ACCOUNT);
                userRepository.save(user);
                return ResponseEntity
                        .ok()
                        .build();
            } else if (user.getUserStatus().equals(UserStatusOpenApi.CONFIRMED_ACCOUNT)) {
                log.debug("Account is already confirmed!");
                return ResponseEntity
                        .badRequest()
                        .build();
            }
        }
        log.debug("User with such user id not found. Link isn't valid");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

}
