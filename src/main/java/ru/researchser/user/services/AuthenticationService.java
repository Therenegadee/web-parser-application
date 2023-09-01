package ru.researchser.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.researchser.user.models.authentication.AuthenticationRequest;
import ru.researchser.user.models.authentication.AuthenticationResponse;
import ru.researchser.user.models.authentication.RegisterRequest;
import ru.researchser.user.models.userModels.User;
import ru.researchser.user.models.userModels.UserRole;
import ru.researchser.user.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User
                .builder()
                .name(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(UserRole.USER)
                .build();
        userRepository.save(user);
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow();
        var jwToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwToken)
                .build();
    }
}
