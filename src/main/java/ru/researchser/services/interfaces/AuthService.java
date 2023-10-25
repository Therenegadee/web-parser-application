package ru.researchser.services.interfaces;

import org.springframework.http.ResponseEntity;
import ru.researchser.openapi.model.JwtResponseOpenApi;
import ru.researchser.openapi.model.LoginRequestOpenApi;
import ru.researchser.openapi.model.SignupRequestOpenApi;

public interface AuthService {
    ResponseEntity<Void> registerUser(SignupRequestOpenApi signUpRequest);

    ResponseEntity<Void> activateUser(String activationToken);

    ResponseEntity<JwtResponseOpenApi> authenticateUser(LoginRequestOpenApi loginRequest);
}
