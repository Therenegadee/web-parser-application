package parser.userService.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.openapi.model.JwtResponseOpenApi;
import parser.userService.openapi.model.LoginRequestOpenApi;
import parser.userService.openapi.model.SignupRequestOpenApi;

public interface AuthService {
    ResponseEntity<Void> registerUser(SignupRequestOpenApi signUpRequest);

    ResponseEntity<Void> activateUser(String activationToken);

    ResponseEntity<JwtResponseOpenApi> authenticateUser(LoginRequestOpenApi loginRequest);

}
