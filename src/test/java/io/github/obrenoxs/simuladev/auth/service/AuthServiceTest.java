package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.auth.dto.request.LoginRequest;
import io.github.obrenoxs.simuladev.auth.dto.result.LoginResult;
import io.github.obrenoxs.simuladev.auth.refresh.service.RefreshTokenService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
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

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

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
    }
}
