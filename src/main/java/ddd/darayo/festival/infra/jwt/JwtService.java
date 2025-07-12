package ddd.darayo.festival.infra.jwt;

import ddd.darayo.festival.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expire-length}")
    private Long expireLength;

    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(getSigningKey(secretKey))
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public Long getUserId(String token) {
        String subject = getSubject(token);
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new JwtException("Invalid user ID in token", e);
        }
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
