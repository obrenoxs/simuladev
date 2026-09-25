package io.github.obrenoxs.simuladev.auth.service;

import io.github.obrenoxs.simuladev.auth.dto.request.LoginRequest;
import io.github.obrenoxs.simuladev.auth.dto.response.LoginResponse;
import io.github.obrenoxs.simuladev.auth.dto.result.LoginResult;
import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import io.github.obrenoxs.simuladev.auth.refresh.service.RefreshTokenService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("E-mail ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());

        String  refreshToken = refreshTokenService.create(user);

        LoginResponse loginResponse = new LoginResponse(user.getId(), token, user.getName());

        return new LoginResult(loginResponse, refreshToken);
    }
}
