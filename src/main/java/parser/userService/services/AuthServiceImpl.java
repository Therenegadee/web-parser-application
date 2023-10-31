package parser.userService.services;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parser.userService.DAO.interfaces.EmailTokenDao;
import parser.userService.exceptions.BadRequestException;
import parser.userService.exceptions.NotFoundException;
import parser.userService.models.EmailToken;
import parser.userService.models.User;
import parser.userService.models.enums.ActivationStatus;
import parser.userService.models.enums.TokenType;
import parser.userService.services.interfaces.AuthService;
import parser.userService.services.interfaces.UserService;
import parser.userService.utils.JwtUtils;
import user.openapi.model.JwtResponseOpenApi;
import user.openapi.model.LoginRequestOpenApi;
import user.openapi.model.SignupRequestOpenApi;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j
@PropertySource(value = "classpath:application.yml")
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final MailSenderService senderService;
    private final EmailTokenDao emailTokenDao;
    private final BcryptFunction bcrypt;
    @Value("${crypto-util.pepper}")
    private String pepper;
    @Override
    public ResponseEntity<Void> registerUser(SignupRequestOpenApi signUpRequest) {
        String password = encryptPassword(signUpRequest.getPassword());
        signUpRequest.setPassword(password);
        User user = userService.saveOrUpdateUser(signUpRequest);
        senderService.sendVerificationEmail(user.getEmail(), new EmailToken(user));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(LoginRequestOpenApi loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        if(!passwordIsCorrect(loginRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }
        return ResponseEntity
                .ok(generateAuthToken(user));
    }

    @Override
    public ResponseEntity<Void> activateUser(String activationToken) {
        User user = checkActivationTokenIsValid(activationToken);
        user.setActivationStatus(ActivationStatus.VERIFIED);
        userService.saveOrUpdateUser(user);
        return ResponseEntity
                .ok()
                .build();

    }

    private String encryptPassword(String password){
        Hash hash = Password.hash(password)
                .addPepper(pepper)
                .with(bcrypt);
        return hash.getResult();
    }

    private boolean passwordIsCorrect(String dbPassword, String inputPassword){
        return Password.check(dbPassword, inputPassword)
                .addPepper(pepper)
                .with(bcrypt);
    }

    private User checkActivationTokenIsValid(String activationToken) {
        EmailToken token = emailTokenDao.findByToken(activationToken)
                .orElseThrow(() -> new NotFoundException("The Link isn't Valid"));
        TokenType tokenType = token.getTokenType();
        Date tokenExpirationDate = token.getExpirationDate();
        Date currentDate = new Date();
        if (currentDate.before(tokenExpirationDate)) {
            User user = token.getUser();
            ActivationStatus activationStatus = user.getActivationStatus();
            if (!activationStatus.equals(ActivationStatus.VERIFIED) && tokenType.equals(TokenType.ACTIVATION)) {
                emailTokenDao.delete(token);
                return user;
            } else {
                throw new BadRequestException("You already verified your email!");
            }
        } else {
            throw new BadRequestException("The token expired or token type isn't valid");
        }
    }

    private JwtResponseOpenApi generateAuthToken(User user) {
        String jwt = jwtUtils.generateJwtToken(user);
        user.setPassword(null);
        return new JwtResponseOpenApi(
                user.getId(),
                jwt,
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().getValue())
                        .collect(Collectors.toList()));
    }
}
