package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private SecretKey key;

    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(UUID id, UserRole role) {
        Instant now = Instant.now();
        Instant expiration = now.plus(Duration.ofMinutes(15));

        String token = Jwts.builder()
                .subject(id.toString())
                .claim("role", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();

        return token;
    }
}
