package ru.researchser.user.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String extractUserEmail(String jwToken) {
        return extractClaim(jwToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwToken);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims (String jwToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJwt(jwToken)
                .getBody();
    }

    public String generateToken (UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken (Map<String, Object> claims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY.toString());
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    public boolean isTokenValid(String jwToken, UserDetails userDetails) {
        final String userEmail = extractUserEmail(jwToken);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(jwToken);
    }

    private boolean isTokenExpired(String jwToken) {
        return extractExpiration(jwToken).before(new Date());
    }

    private Date extractExpiration(String jwToken) {
        return extractClaim(jwToken, Claims::getExpiration);
    }
}
