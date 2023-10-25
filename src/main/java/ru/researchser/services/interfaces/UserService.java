package ru.researchser.services.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import ru.researchser.models.User;
import ru.researchser.openapi.model.SignupRequestOpenApi;
import ru.researchser.openapi.model.UserOpenApi;

public interface UserService {
    ResponseEntity<UserOpenApi> showUserInfo(Long id);

    UserDetails loadUserByUsername(String username);


    User saveOrUpdateUser(SignupRequestOpenApi signUpRequest);

    User saveOrUpdateUser(UserOpenApi userOpenApi);

    User saveOrUpdateUser(User user);

    User updateUser(User user);

    User updateUser(UserOpenApi userOpenApi);
}
