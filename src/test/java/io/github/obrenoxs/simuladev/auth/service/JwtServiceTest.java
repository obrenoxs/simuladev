package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static io.github.obrenoxs.simuladev.auth.service.JwtService.TOKEN_TYPE_ACCESS;

public class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "test-secret-test-secret-test-secret-12";

    @BeforeEach
    void setUp() throws Exception {

        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", SECRET);
        ReflectionTestUtils.invokeMethod(jwtService, "init");
    }

    @Test
    void generateTokenShouldProduceTokenWithUserIdRoleAndAccessType() {

        UUID id = UUID.randomUUID();

        String token = jwtService.generateToken(id, UserRole.USER);

        Assertions.assertEquals(id, jwtService.extractUserId(token));
        Assertions.assertEquals(UserRole.USER, jwtService.extractRole(token));
        Assertions.assertEquals(TOKEN_TYPE_ACCESS, jwtService.extractType(token));
    }

    @Test
    void generateRefreshTokenShouldProduceTokenWithUserIdAndRefreshType() {

        UUID id = UUID.randomUUID();

        String token = jwtService.generateRefreshToken(id);

        Assertions.assertEquals(id, jwtService.extractUserId(token));
        Assertions.assertEquals(JwtService.TOKEN_TYPE_REFRESH, jwtService.extractType(token));
    }

    @Test
    void extractUserIdShouldThrowJwtExceptionWhenTokenIsTampered() {

        UUID id = UUID.randomUUID();

        String token = jwtService.generateToken(id, UserRole.USER);

        String[] parts = token.split("\\.");

        char original = parts[2].charAt(0);
        char replacement = original == 'A' ? 'B' : 'A';

        parts[2] = replacement + parts[2].substring(1);

        String tamperedToken = String.join(".", parts);

        Assertions.assertEquals(id, jwtService.extractUserId(token));

        Assertions.assertThrows(JwtException.class, () -> {
            jwtService.extractUserId(tamperedToken);
        });
    }

    @Test
    void extractUserIdShouldThrowJwtExceptionWhenTokenIsSignedWithAnotherKey() {

        SecretKey serviceKey = Keys.hmacShaKeyFor(SECRET.getBytes());
        SecretKey otherKey = Keys.hmacShaKeyFor("other-secret-other-secret-other-secret-1".getBytes());

        String token = buildToken(otherKey, Date.from(Instant.now().plus(Duration.ofHours(1))));

        Assertions.assertThrows(JwtException.class, () -> {
            jwtService.extractUserId(token);
        });
    }

    @Test
    void extractUserIdShouldThrowExpiredJwtExceptionWhenTokenIsExpired() {

        SecretKey serviceKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        String token = buildToken(serviceKey, Date.from(Instant.now().minus(Duration.ofHours(1))));

        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtService.extractUserId(token);
        });
    }

    private String buildToken(SecretKey key, Date expiration) {

        String token = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("role", UserRole.ADMIN.name())
                .claim("type", TOKEN_TYPE_ACCESS)
                .issuedAt(Date.from(Instant.now().minus(Duration.ofHours(2))))
                .expiration(expiration)
                .signWith(key)
                .compact();

        return token;
    }
}
