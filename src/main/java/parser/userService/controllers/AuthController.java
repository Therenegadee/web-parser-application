package parser.userService.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
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
@Log4j
public class AuthController implements AuthorizationApiDelegate {
    private final AuthService authService;

    @Override
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(@Valid @RequestBody LoginRequestOpenApi loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignupRequestOpenApi signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @Override
    @PatchMapping("/activation")
    public ResponseEntity<Void> activateUser(@RequestParam("activationToken") String activationToken) {
        return authService.activateUser(activationToken);
    }
}
