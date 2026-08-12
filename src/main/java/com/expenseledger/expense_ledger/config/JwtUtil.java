package com.expenseledger.expense_ledger.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecretString;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
    }
    private final long expirationMs = 1000 * 60 * 60 * 10; // 10 hours expiration time
    public String generateToken(String email){
        return Jwts.builder()//starts constructing a new token
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)//it signs the token using your private secretKey- this is a security step
                .compact();
    }
    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }
    //validate tokens expiry
    public boolean isTokenValid(String token){
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    //verify token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)//checks the signature of the token and decodes it
                .getPayload();
    }
}
