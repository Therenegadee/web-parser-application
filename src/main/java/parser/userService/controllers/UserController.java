package parser.userService.controllers;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parser.userService.mappers.openapi.UserMapper;
import parser.userService.services.interfaces.UserService;
import user.openapi.api.UserApiDelegate;
import user.openapi.model.UserOpenApi;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController implements UserApiDelegate {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    @Observed
    @GetMapping("/id/{id}")
    public ResponseEntity<UserOpenApi> showUserInfoById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.toOpenApi(userService.showUserInfo(id)));
    }

    @Override
    @Observed
    @GetMapping("/username/{username}")
    public ResponseEntity<UserOpenApi> showUserInfoByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userMapper.toOpenApi(userService.showUserInfo(username)));
    }
}
