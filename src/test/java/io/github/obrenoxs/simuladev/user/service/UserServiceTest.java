package io.github.obrenoxs.simuladev.user.service;

import io.github.obrenoxs.simuladev.auth.service.JwtService;
import io.github.obrenoxs.simuladev.email.EmailService;
import io.github.obrenoxs.simuladev.user.dto.request.UserRequest;
import io.github.obrenoxs.simuladev.user.exception.EmailAlreadyExistsException;
import io.github.obrenoxs.simuladev.user.mapper.UserMapper;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Test
    void createShouldThrowEmailAlreadyExistsExceptionWhenEmailIsTaken() {

        UserRequest request = new UserRequest("UserTest", "userTest@example.com", "12345678");

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
           userService.create(request);
        });

        verifyNoInteractions(passwordEncoder, userMapper, jwtService, emailService);
        verify(userRepository, never()).save(any());
    }
}
