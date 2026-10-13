package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {

        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-test-secret-test-secret-12");
        ReflectionTestUtils.invokeMethod(jwtService, "init");
    }

    @Test
    void generateTokenShouldProduceTokenWithUserIdRoleAndAccessType() {

        UUID id = UUID.randomUUID();

        String token = jwtService.generateToken(id, UserRole.USER);

        Assertions.assertEquals(id, jwtService.extractUserId(token));
        Assertions.assertEquals(UserRole.USER, jwtService.extractRole(token));
        Assertions.assertEquals(JwtService.TOKEN_TYPE_ACCESS, jwtService.extractType(token));
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
}
