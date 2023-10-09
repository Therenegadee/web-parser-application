package ru.researchser.controllers;

import org.springframework.http.ResponseEntity;
import ru.researchser.openapi.api.UserApiDelegate;
import ru.researchser.openapi.model.UserOpenApi;

public class UserController implements UserApiDelegate {
    @Override
    public ResponseEntity<UserOpenApi> showUserInfo(Integer id) {
        return UserApiDelegate.super.showUserInfo(id);
    }
}
