package io.github.obrenoxs.simuladev.auth.controller;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.auth.refresh.repository.RefreshTokenRepository;
import io.github.obrenoxs.simuladev.email.EmailService;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AuthFlowIT extends AbstractIntegrationTest {

    private static final String EMAIL = "flow-user@example.com";
    private static final String PASSWORD = "12345678";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @MockitoBean
    private EmailService emailService;

    @AfterEach
    void cleanUp() {
        userRepository.findByEmail(EMAIL).ifPresent(user -> {
            refreshTokenRepository.deleteAll(refreshTokenRepository.findAllByUserId(user.getId()));
            userRepository.delete(user);
        });
    }

    @Test
    void userShouldSignUpVerifyEmailAndLogin() throws Exception {

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpBody()))
                .andExpect(status().isCreated());

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendVerificationEmail(eq(EMAIL), tokenCaptor.capture());
        String verificationToken = tokenCaptor.getValue();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody()))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", verificationToken))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String setCookie = loginResult.getResponse().getHeader("Set-Cookie");
        assertNotNull(setCookie);
        assertTrue(setCookie.startsWith("refreshToken="));
        assertTrue(setCookie.contains("HttpOnly"));
    }

    @Test
    void userShouldAccessProtectedRouteRefreshTokenAndLogout() throws Exception {

        signUpAndVerifyEmail();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody()))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.token");
        String refreshCookieValue = extractRefreshTokenValue(loginResult.getResponse().getHeader("Set-Cookie"));

        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL));

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshCookieValue)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String newAccessToken = JsonPath.read(refreshResult.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + newAccessToken))
                .andExpect(status().isOk());

        MvcResult logoutResult = mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(new Cookie("refreshToken", refreshCookieValue)))
                .andExpect(status().isNoContent())
                .andReturn();

        String clearingCookie = logoutResult.getResponse().getHeader("Set-Cookie");
        assertNotNull(clearingCookie);
        assertTrue(clearingCookie.contains("Max-Age=0"));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshCookieValue)))
                .andExpect(status().isUnauthorized());
    }

    private void signUpAndVerifyEmail() throws Exception {

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpBody()))
                .andExpect(status().isCreated());

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendVerificationEmail(eq(EMAIL), tokenCaptor.capture());

        mockMvc.perform(get("/api/v1/auth/verify-email")
                        .param("token", tokenCaptor.getValue()))
                .andExpect(status().isOk());
    }

    private String extractRefreshTokenValue(String setCookieHeader) {
        int start = "refreshToken=".length();
        int end = setCookieHeader.indexOf(';');
        return setCookieHeader.substring(start, end);
    }

    private String signUpBody() {
        return """
                {"name": "UserTest", "email": "%s", "password": "%s"}
                """.formatted(EMAIL, PASSWORD);
    }

    private String loginBody() {
        return """
                {"email": "%s", "password": "%s"}
                """.formatted(EMAIL, PASSWORD);
    }
}
