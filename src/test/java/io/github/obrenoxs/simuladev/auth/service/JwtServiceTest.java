package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.user.enums.UserRole;
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

        Assertions.assertEquals(jwtService.extractUserId(token), id);
        Assertions.assertEquals(jwtService.extractRole(token), UserRole.USER);
        Assertions.assertEquals(jwtService.extractType(token), JwtService.TOKEN_TYPE_ACCESS);
    }
}
