package ru.researchser.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.researchser.mailSender.services.MailSenderService;
import ru.researchser.mailSender.services.SendMailRequest;
import ru.researchser.security.payloads.request.LoginRequest;
import ru.researchser.security.payloads.request.SignupRequest;
import ru.researchser.security.payloads.response.JwtResponse;
import ru.researchser.security.payloads.response.MessageResponse;
import ru.researchser.security.services.JwtUtils;
import ru.researchser.security.user.UserDetailsImpl;
import ru.researchser.security.utils.CryptoUtil;
import ru.researchser.user.models.Role;
import ru.researchser.user.models.User;
import ru.researchser.user.models.enums.ERole;
import ru.researchser.user.models.enums.UserStatus;
import ru.researchser.user.repositories.RoleRepository;
import ru.researchser.user.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Authentication API")
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
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Login operation",
            operationId = "authenticateUser"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfuly!", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Account with such email is already in use!")
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return ResponseEntity
                    .ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Authentication error! Incorrect login/password input"));
        }
    }

    @PostMapping("/signup")
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Register operation",
            operationId = "registerUser"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully and waits for email verification!"),
            @ApiResponse(responseCode = "400", description = "Account with such email is already in use!"),
            @ApiResponse(responseCode = "409", description = "Account with such username is already in use!")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Account with such username is already in use!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Account with such email is already in use!"));
        }
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setUserStatus(UserStatus.WAIT_FOR_EMAIL_VERIFICATION);

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
                .body(new MessageResponse("User registered successfully and waits for email verification!"));
    }

    @PatchMapping("/activation")
    @PreAuthorize("isAnonymous()")
    @Operation(
            summary = "Activation user via email operation",
            operationId = "activateUser"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account was confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Account is already confirmed!"),
            @ApiResponse(responseCode = "404", description = "The link isn't valid. User with such user id not found.")
    })
    public ResponseEntity<?> activateUser(@RequestParam("id") String cryptoUserId) {
        Long userId = cryptoUtil.idOf(cryptoUserId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();
            if (user.getUserStatus().equals(UserStatus.WAIT_FOR_EMAIL_VERIFICATION)) {
                user.setUserStatus(UserStatus.CONFIRMED_ACCOUNT);
                userRepository.save(user);
                return ResponseEntity
                        .ok(new MessageResponse("Account confirmed successfully"));
            } else if (user.getUserStatus().equals(UserStatus.CONFIRMED_ACCOUNT)) {
                log.debug("Account is already confirmed!");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Account is already confirmed!"));
            }
        }
        log.debug("User with such user id not found. Link isn't valid");
        return ResponseEntity
                .notFound()
                .build();
    }

}
