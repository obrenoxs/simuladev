package io.github.obrenoxs.simuladev.concept.controller;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.auth.service.JwtService;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ConceptControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Test
    void findAllShouldReturnConceptsWhenTokenIsValid() throws Exception {

        String token = jwtService.generateToken(UUID.randomUUID(), UserRole.USER);

        mockMvc.perform(get("/api/v1/concepts")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void findAllShouldReturnUnauthorizedWhenTokenIsMissing() throws Exception {

        mockMvc.perform(get("/api/v1/concepts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findAllShouldReturnUnauthorizedWhenRefreshTokenIsUsedAsBearer() throws Exception {

        String refreshToken = jwtService.generateRefreshToken(UUID.randomUUID());

        mockMvc.perform(get("/api/v1/concepts")
                        .header("Authorization", "Bearer " + refreshToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findAllShouldReturnUnauthorizedWhenAccessTokenIsExpired() throws Exception {

        String validToken = buildAccessToken(Date.from(Instant.now().plus(Duration.ofHours(1))));

        mockMvc.perform(get("/api/v1/concepts")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());

        String expiredToken = buildAccessToken(Date.from(Instant.now().minus(Duration.ofHours(1))));

        mockMvc.perform(get("/api/v1/concepts")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    private String buildAccessToken(Date expiration) {

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("role", UserRole.USER.name())
                .claim("type", JwtService.TOKEN_TYPE_ACCESS)
                .issuedAt(Date.from(Instant.now().minus(Duration.ofHours(2))))
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
}
