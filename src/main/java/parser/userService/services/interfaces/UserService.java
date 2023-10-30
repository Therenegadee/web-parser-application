package parser.userService.services.interfaces;

import org.springframework.http.ResponseEntity;
import parser.userService.models.User;
import user.openapi.model.SignupRequestOpenApi;
import user.openapi.model.UserOpenApi;

public interface UserService {
    ResponseEntity<UserOpenApi> showUserInfo(Long id);
    ResponseEntity<UserOpenApi> showUserInfo(String username);

    User saveOrUpdateUser(SignupRequestOpenApi signUpRequest);

    User saveOrUpdateUser(UserOpenApi userOpenApi);

    User saveOrUpdateUser(User user);

    User updateUser(User user);

    User updateUser(UserOpenApi userOpenApi);

    User findByUsername(String username);
}
