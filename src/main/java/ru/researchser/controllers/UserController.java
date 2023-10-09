package ru.researchser.controllers;

import org.springframework.http.ResponseEntity;
import ru.researchser.openapi.api.UserApiDelegate;
import ru.researchser.openapi.model.User;

public class UserController implements UserApiDelegate {
    @Override
    public ResponseEntity<User> showUserInfo(Integer id) {
        return UserApiDelegate.super.showUserInfo(id);
    }
}
