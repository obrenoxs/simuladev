package io.github.obrenoxs.simuladev.auth.refresh.service;

import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import io.github.obrenoxs.simuladev.auth.refresh.repository.RefreshTokenRepository;
import io.github.obrenoxs.simuladev.auth.service.JwtService;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String create(User user) {
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        String encodedRefreshToken = passwordEncoder.encode(refreshToken);

        LocalDateTime expirationDate = LocalDateTime.now().plusDays(JwtService.REFRESH_TOKEN_VALIDITY_DAYS);

        RefreshToken refreshTokenEntity = new RefreshToken(null, user, encodedRefreshToken, expirationDate);
        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
}
