package ru.researchser.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "RhvEDNV8oyHRSYUpSuJCDEoXBOeBnfP6";


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
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(jwToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken (User user){
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken (Map<String, Object> claims, User user){
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
