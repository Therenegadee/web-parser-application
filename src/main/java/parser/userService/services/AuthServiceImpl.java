package parser.userService.services;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import parser.userService.exceptions.BadRequestException;
import parser.userService.exceptions.NotFoundException;
import parser.userService.models.EmailToken;
import parser.userService.models.User;
import parser.userService.models.enums.ActivationStatus;
import parser.userService.models.enums.TokenType;
import parser.userService.openapi.model.JwtResponseOpenApi;
import parser.userService.openapi.model.LoginRequestOpenApi;
import parser.userService.openapi.model.SignupRequestOpenApi;
import parser.userService.DAO.interfaces.EmailTokenDao;
import parser.userService.openapi.model.UserOpenApi;
import parser.userService.security.JwtUtils;
import parser.userService.services.interfaces.AuthService;
import parser.userService.services.interfaces.UserService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final MailSenderService senderService;
    private final EmailTokenDao emailTokenDao;

    @Override
    public ResponseEntity<Void> registerUser(SignupRequestOpenApi signUpRequest) {
        User user = userService.saveOrUpdateUser(signUpRequest);
        senderService.sendVerificationEmail(user.getEmail(), new EmailToken(user));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<JwtResponseOpenApi> authenticateUser(LoginRequestOpenApi loginRequest) {
        try {
            return ResponseEntity
                    .ok(generateAuthToken(loginRequest.getUsername()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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

    private JwtResponseOpenApi generateAuthToken(String username) {
        User user = (User) userService.loadUserByUsername(username);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(user);
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        user.setPassword(null);
        return new JwtResponseOpenApi(user.getId(), jwt, user.getUsername());
    }
}
