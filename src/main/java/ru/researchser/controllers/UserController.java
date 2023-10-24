package ru.researchser.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.researchser.openapi.api.UserApiDelegate;
import ru.researchser.openapi.model.UserOpenApi;
@RestController
@RequestMapping("/user")
public class UserController implements UserApiDelegate {
    @Override
    public ResponseEntity<UserOpenApi> showUserInfo(Long id) {
        return UserApiDelegate.super.showUserInfo(id);
    }
}
