package parser.userService.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
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
import parser.userService.services.interfaces.UserService;
import user.openapi.model.SignupRequestOpenApi;
import user.openapi.model.UserOpenApi;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final RoleDao roleDao;

    @Override
    public ResponseEntity<UserOpenApi> showUserInfo(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(()->new NotFoundException(String.format("user with id %d wasn't found", id)));
        return ResponseEntity.ok(userMapper.toOpenApi(user));
    }

    @Override
    public ResponseEntity<UserOpenApi> showUserInfo(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(()->new NotFoundException(String.format("user with id %s wasn't found", username)));
        return ResponseEntity.ok(userMapper.toOpenApi(user));
    }

    @Override
    public User saveOrUpdateUser(SignupRequestOpenApi signUpRequest) {
        checkUserAlreadyExists(signUpRequest);
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        user.setActivationStatus(ActivationStatus.WAIT_FOR_EMAIL_VERIFICATION);
        Set<Role> roles = user.getRoles();
        if(roles.isEmpty()) {
            roles.add(new Role(ERole.ROLE_USER));
            user.setRoles(roles);
        } else {
            user.setRoles(roles);
        }
        return saveOrUpdateUser(user);
    }

    @Override
    public User saveOrUpdateUser(UserOpenApi userOpenApi) {
        User user = userMapper.toUser(userOpenApi);
        return saveOrUpdateUser(user);
    }
    @Override
    public User saveOrUpdateUser(User user) {
        if(Objects.nonNull(user.getPassword())) {
            user.setPassword(user.getPassword());
        }
        if (Objects.isNull(user.getId())) {
            Optional<User> userByUsernameOpt = userDao.findByUsername(user.getUsername());
            userByUsernameOpt.ifPresent(value -> user.setId(value.getId()));
            Optional<User> userByEmailOpt = userDao.findByEmail(user.getEmail());
            userByEmailOpt.ifPresent(value -> user.setId(value.getId()));
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

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s wasn't found", username)));
        Set<Role> roles = roleDao.findAllByUserId(user.getId());
        if(roles.isEmpty()) {
            throw new NotFoundException(String.format("User with id %d doesn't have any Role", user.getId()));
        }
        user.setRoles(roles);
        return user;
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
