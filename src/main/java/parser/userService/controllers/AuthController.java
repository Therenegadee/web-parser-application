package parser.userService.controllers;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parser.userService.services.interfaces.AuthService;
import user.openapi.api.AuthorizationApiDelegate;
import user.openapi.model.JwtResponseOpenApi;
import user.openapi.model.LoginRequestOpenApi;
import user.openapi.model.SignupRequestOpenApi;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController implements AuthorizationApiDelegate {
    private final AuthService authService;

    @Override
    @Observed
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(@Valid @RequestBody LoginRequestOpenApi loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Override
    @Observed
    @PostMapping("/signup")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignupRequestOpenApi signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Override
    @Observed
    @PatchMapping("/activation")
    public ResponseEntity<Void> activateUser(@RequestParam("activationToken") String activationToken) {
        return authService.activateUser(activationToken);
    }
}
