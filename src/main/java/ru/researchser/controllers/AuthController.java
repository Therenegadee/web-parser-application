package ru.researchser.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.researchser.openapi.api.AuthorizationApiDelegate;
import ru.researchser.services.interfaces.AuthService;
import ru.researchser.openapi.model.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Log4j
public class AuthController implements AuthorizationApiDelegate {
    private final AuthService authService;

    @Override
    @PostMapping("/signin")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(@Valid @RequestBody LoginRequestOpenApi loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Override
    @PostMapping("/signup")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignupRequestOpenApi signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Override
    @PatchMapping("/activation")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<Void> activateUser(@RequestParam("activationToken") String activationToken) {
        return authService.activateUser(activationToken);
    }
}
