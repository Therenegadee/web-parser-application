package parser.userService.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parser.userService.DAO.interfaces.UserDao;
import parser.userService.mappers.openapi.UserMapper;
import parser.userService.models.User;
import user.openapi.model.JwtResponseOpenApi;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;
    private final UserDao userDao;
    private final UserMapper userMapper;

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    public Claims getAllClaimsByToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtResponseOpenApi validateToken(String jwtToken){
        String username = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
        Optional<User> userOpt = userDao.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOpt.get();
        JwtResponseOpenApi jwtResponseOpenApi = new JwtResponseOpenApi(
                user.getId(),
                generateJwtToken(user),
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().getValue())
                        .collect(Collectors.toList())
        );
        return jwtResponseOpenApi;
    }

    public boolean isTokenValid(String authToken) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
