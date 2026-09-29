package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.auth.dto.request.LoginRequest;
import io.github.obrenoxs.simuladev.auth.dto.request.ResendVerificationRequest;
import io.github.obrenoxs.simuladev.auth.dto.response.LoginResponse;
import io.github.obrenoxs.simuladev.auth.dto.result.LoginResult;
import io.github.obrenoxs.simuladev.auth.email.EmailService;
import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import io.github.obrenoxs.simuladev.auth.refresh.service.RefreshTokenService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.exception.EmailNotVerifiedException;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Usuário precisa validar o E-mail");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        String  refreshToken = refreshTokenService.create(user);

        LoginResponse loginResponse = new LoginResponse(user.getId(), token, user.getName());

        return new LoginResult(loginResponse, refreshToken);
    }

    public String refresh(String refreshTokenValue) {
        String hash = RefreshTokenService.hashToken(refreshTokenValue);

        RefreshToken refreshToken = refreshTokenService.findByHash(hash);

        if (refreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Acesso expirado, faça login novamente");
        }

        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user.getId(), user.getRole());

        return accessToken;
    }

    @Transactional
    public void verifyEmail(String token) {
        String type = jwtService.extractType(token);

        if (!type.equals(JwtService.TOKEN_TYPE_EMAIL_VERIFICATION)) {
            throw new BadCredentialsException("Acesso inválido");
        }

        UUID userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Usuário inválido"));

        user.setEmailVerified(true);
        user = userRepository.save(user);
    }

    @Transactional
    public void resendVerificationEmail(ResendVerificationRequest emailRequest) {
        String email = emailRequest.email();

        Optional<User> pendingUser = userRepository.findByEmail(email)
                .filter(user -> !user.isEmailVerified());

        if (pendingUser.isEmpty()) {
            return;
        }

       User user = pendingUser.get();

        String mailToken = jwtService.generateEmailVerificationToken(user.getId());

        emailService.sendVerificationEmail(user.getEmail(), mailToken);
    }

    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        refreshTokenService.deleteToken(refreshToken);
    }

}
