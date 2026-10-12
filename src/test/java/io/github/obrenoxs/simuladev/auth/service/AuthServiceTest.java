package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.auth.dto.request.LoginRequest;
import io.github.obrenoxs.simuladev.auth.dto.result.LoginResult;
import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import io.github.obrenoxs.simuladev.auth.refresh.service.RefreshTokenService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.github.obrenoxs.simuladev.user.exception.EmailNotVerifiedException;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    private User user;
    private LoginRequest request;

    @BeforeEach
    void setUp() throws Exception {

        user = new User(UUID.randomUUID(), "UserTest", "userTest@example.com", "Java + Spring", "ESTAGIARIO", 0, UserRole.USER, "hashDaSenha");
        user.setEmailVerified(true);
        request = new LoginRequest("userTest@example.com", "12345678");

    }

    @Test
    void loginShouldThrowBadCredentialsExceptionWhenEmailNotFound() {

        when(userRepository.findByEmail("userTest@example.com"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            LoginResult result = authService.login(request);
        });

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void loginShouldThrowBadCredentialsExceptionWhenPasswordIsWrong() {

        when(userRepository.findByEmail("userTest@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("12345678", "hashDaSenha"))
                .thenReturn(false);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            LoginResult result = authService.login(request);
        });

        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void loginShouldThrowEmailNotVerifiedExceptionWhenPasswordIsCorrectButEmailNotVerified() {

        user.setEmailVerified(false);

        when(userRepository.findByEmail("userTest@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("12345678", "hashDaSenha"))
                .thenReturn(true);

        Assertions.assertThrows(EmailNotVerifiedException.class, () -> {
            LoginResult result = authService.login(request);
        });

        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void loginShouldThrowBadCredentialsExceptionWhenPasswordIsWrongAndEmailNotVerified() {

        user.setEmailVerified(false);

        when(userRepository.findByEmail("userTest@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("12345678", "hashDaSenha"))
                .thenReturn(false);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            LoginResult result = authService.login(request);
        });

        verifyNoInteractions(jwtService, refreshTokenService);
    }

    @Test
    void loginShouldReturnTokensWhenCredentialsAreValidAndEmailIsVerified() {

        when(userRepository.findByEmail("userTest@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("12345678", "hashDaSenha"))
                .thenReturn(true);

        when(jwtService.generateToken(user.getId(), user.getRole()))
                .thenReturn("access-token");

        when(refreshTokenService.create(user))
                .thenReturn("refresh-token");

        LoginResult result = authService.login(request);

        Assertions.assertEquals(result.refreshToken(), "refresh-token");
        Assertions.assertEquals(result.response().token(), "access-token");
        Assertions.assertEquals(result.response().id(), user.getId());
        Assertions.assertEquals(result.response().name(), user.getName());

        verify(refreshTokenService).create(user);
    }

    @Test
    void refreshShouldThrowBadCredentialsExceptionWhenTokenIsExpired() {

        String rawToken = "raw-refresh-token";
        String hash = RefreshTokenService.hashToken(rawToken);
        RefreshToken storedToken = new RefreshToken(UUID.randomUUID(), user, hash, LocalDateTime.now().minusDays(1));

        when(refreshTokenService.findByHash(hash))
                .thenReturn(storedToken);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            authService.refresh(rawToken);
        });

        verifyNoInteractions(jwtService);
    }

    @Test
    void refreshShouldReturnNewAccessTokenWhenTokenIsValid() {

        String rawToken = "raw-refresh-token";
        String hash = RefreshTokenService.hashToken(rawToken);
        RefreshToken storedToken = new RefreshToken(UUID.randomUUID(), user, hash, LocalDateTime.now().plusDays(1));

        when(refreshTokenService.findByHash(hash))
                .thenReturn(storedToken);


        when(jwtService.generateToken(user.getId(), user.getRole()))
                .thenReturn("new-access-token");

        String refreshToken = authService.refresh(rawToken);

        Assertions.assertEquals("new-access-token", refreshToken);
        verify(jwtService).generateToken(user.getId(), user.getRole());
    }

    @Test
    void verifyEmailShouldThrowBadCredentialsExceptionWhenTokenTypeIsNotEmailVerification() {

        when(jwtService.extractType("any-token"))
                .thenReturn(JwtService.TOKEN_TYPE_ACCESS);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            authService.verifyEmail("any-token");
        });

        verifyNoInteractions(userRepository);
    }

    @Test
    void verifyEmailShouldThrowBadCredentialsExceptionWhenUserNoLongerExists() {

        String token = "any-token";

        when(jwtService.extractType(token))
                .thenReturn(JwtService.TOKEN_TYPE_EMAIL_VERIFICATION);

        when(jwtService.extractUserId(token))
                .thenReturn(user.getId());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
                authService.verifyEmail("any-token");
        });

        verify(userRepository, never()).save(any());
    }
}
