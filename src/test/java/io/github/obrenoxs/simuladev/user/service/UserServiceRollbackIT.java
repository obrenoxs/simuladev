package io.github.obrenoxs.simuladev.user.service;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.email.EmailService;
import io.github.obrenoxs.simuladev.user.dto.request.UserRequest;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

public class UserServiceRollbackIT extends AbstractIntegrationTest {

    private static final String EMAIL = "rollback-user@example.com";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private EmailService emailService;

    @AfterEach
    void cleanUp() {
        userRepository.findByEmail(EMAIL).ifPresent(userRepository::delete);
    }

    @Test
    void createShouldNotKeepUserWhenVerificationEmailFails() {

        UserRequest request = new UserRequest("UserTest", EMAIL, "12345678");

        doThrow(new MailSendException("falha simulada"))
                .when(emailService).sendVerificationEmail(anyString(), anyString());

        assertThrows(MailSendException.class, () -> userService.create(request));

        assertTrue(userRepository.findByEmail(EMAIL).isEmpty());
    }
}
