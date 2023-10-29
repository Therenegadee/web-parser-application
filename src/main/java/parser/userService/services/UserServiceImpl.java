package parser.userService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import parser.userService.DAO.interfaces.RoleDao;
import parser.userService.DAO.interfaces.UserDao;
import parser.userService.exceptions.BadRequestException;
import parser.userService.exceptions.NotFoundException;
import parser.userService.mappers.openapi.UserMapper;
import parser.userService.models.Role;
import parser.userService.models.User;
import parser.userService.models.enums.ActivationStatus;
import parser.userService.models.enums.ERole;
import parser.userService.security.JwtUtils;
import parser.userService.services.interfaces.UserService;
import user.openapi.model.JwtResponseOpenApi;
import user.openapi.model.SignupRequestOpenApi;
import user.openapi.model.UserOpenApi;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleDao roleDao;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    @Override
    public ResponseEntity<JwtResponseOpenApi> validateJwtToken(String jwtToken) {
        return ResponseEntity
                .ok(jwtUtils.validateToken(jwtToken));
    }

    @Override
    public ResponseEntity<UserOpenApi> showUserInfo(Long id) {
        return null;
    }

    @Override
    public User saveOrUpdateUser(SignupRequestOpenApi signUpRequest) {
        checkUserAlreadyExists(signUpRequest);
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        user.setActivationStatus(ActivationStatus.WAIT_FOR_EMAIL_VERIFICATION);
        User savedUser = saveOrUpdateUser(user);
        if(user.getRoles().isEmpty()) {
            setRole(savedUser, ERole.ROLE_USER);
        } else {
            setRoles(savedUser, user.getRoles());
        }
        return savedUser;
    }

    @Override
    public User saveOrUpdateUser(UserOpenApi userOpenApi) {
        User user = userMapper.toUser(userOpenApi);
        return saveOrUpdateUser(user);
    }
    @Override
    public User saveOrUpdateUser(User user) {
        if (Objects.nonNull(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        if (Objects.isNull(user.getId())) {
            user.setId(usernameOrEmailExistsCheck(user.getUsername(), user.getEmail()).getId());
        }
        User savedUser = userDao.save(user);
        if(user.getRoles().isEmpty()) {
            setRole(savedUser, ERole.ROLE_USER);
        } else {
            setRoles(savedUser, user.getRoles());
        }
        return savedUser;
    }

    @Override
    public User updateUser(UserOpenApi userOpenApi) {
        User user = userMapper.toUser(userOpenApi);
        return updateUser(user);
    }
    @Override
    public User updateUser(User user) {
        return userDao.update(user);
    }


    public void checkUserAlreadyExists(SignupRequestOpenApi signUpRequest) {
        if (usernameExistsCheck(signUpRequest.getUsername())) {
            throw new BadRequestException(String.format(
                    "User with username %s already exists!", signUpRequest.getUsername()));
        }
        if (emailExistsCheck(signUpRequest.getEmail())) {
            throw new BadRequestException(String.format(
                    "User with email %s already exists!", signUpRequest.getEmail()));
        }
    }

    private User usernameOrEmailExistsCheck(String username, String email) {
        if (usernameExistsCheck(username)) {
            return userDao.findByUsername(username).get();
        } else if (!usernameExistsCheck(username)){
            throw new NotFoundException(String.format(
                    "User with username %s wasn't not found!", username));
        }
        if (emailExistsCheck(email)) {
            return userDao.findByEmail(email).get();
        } else {
            throw new BadRequestException(String.format(
                    "User with email %s wasn't not found!", email));
        }
    }

    private boolean usernameExistsCheck(String username) {
        return userDao.findByUsername(username).isPresent();
    }

    private boolean emailExistsCheck(String email) {
        return userDao.findByEmail(email).isPresent();
    }

    private void setRole(User user, ERole roleName) {
        userDao.addRole(user.getId(), roleName);
    }

    private void setRoles(User user, Set<Role> roles) {
        Set<ERole> roleNames = new HashSet<>();
        roles.forEach(roleName -> roleNames.add(roleName.getName()));
        userDao.addRoles(user.getId(), roleNames);
    }



}
