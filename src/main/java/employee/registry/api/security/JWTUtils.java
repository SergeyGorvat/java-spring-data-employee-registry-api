package employee.registry.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Slf4j
@Component
public class JWTUtils {

    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24;
    private static final long REFRESH_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;

    public JWTUtils() {
        String secretString = "9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b";
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        log.info("Генерация JWT токена для пользователя '{}'", userDetails.getUsername());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", userDetails.getAuthorities())
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        log.info("Генерация Refresh токена для пользователя '{}'", userDetails.getUsername());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
               && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsFunction.apply(claims);
    }
}
