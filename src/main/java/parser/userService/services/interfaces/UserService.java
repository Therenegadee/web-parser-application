package parser.userService.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import parser.userService.models.User;
import user.openapi.model.JwtResponseOpenApi;
import user.openapi.model.SignupRequestOpenApi;
import user.openapi.model.UserOpenApi;

public interface UserService {
    ResponseEntity<UserOpenApi> showUserInfo(Long id);

    ResponseEntity<JwtResponseOpenApi> validateJwtToken(String jwtToken);

    User saveOrUpdateUser(SignupRequestOpenApi signUpRequest);

    User saveOrUpdateUser(UserOpenApi userOpenApi);

    User saveOrUpdateUser(User user);

    User updateUser(User user);

    User updateUser(UserOpenApi userOpenApi);

    User findByUsername(String username);
}
