package parser.userService.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import parser.userService.models.User;
import parser.userService.openapi.model.JwtResponseOpenApi;
import parser.userService.openapi.model.SignupRequestOpenApi;
import parser.userService.openapi.model.UserOpenApi;

public interface UserService {
    ResponseEntity<UserOpenApi> showUserInfo(Long id);

    ResponseEntity<JwtResponseOpenApi> validateJwtToken(String jwtToken);

    UserDetails loadUserByUsername(String username);


    User saveOrUpdateUser(SignupRequestOpenApi signUpRequest);

    User saveOrUpdateUser(UserOpenApi userOpenApi);

    User saveOrUpdateUser(User user);

    User updateUser(User user);

    User updateUser(UserOpenApi userOpenApi);
}
