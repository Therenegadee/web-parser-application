package parser.userService.services.interfaces;

import org.springframework.http.ResponseEntity;
import user.openapi.model.JwtResponseOpenApi;
import user.openapi.model.LoginRequestOpenApi;
import user.openapi.model.SignupRequestOpenApi;

public interface AuthService {
    ResponseEntity<Void> registerUser(SignupRequestOpenApi signUpRequest);

    ResponseEntity<Void> activateUser(String activationToken);

    ResponseEntity<JwtResponseOpenApi> authenticateUser(LoginRequestOpenApi loginRequest);

}
