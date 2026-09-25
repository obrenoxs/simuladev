package io.github.obrenoxs.simuladev.auth.refresh.service;

import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import io.github.obrenoxs.simuladev.auth.refresh.repository.RefreshTokenRepository;
import io.github.obrenoxs.simuladev.auth.service.JwtService;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;


@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public String create(User user) {
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        String encodedRefreshToken = hashToken(refreshToken);

        LocalDateTime expirationDate = LocalDateTime.now().plusDays(JwtService.REFRESH_TOKEN_VALIDITY_DAYS);

        RefreshToken refreshTokenEntity = new RefreshToken(null, user, encodedRefreshToken, expirationDate);
        refreshTokenEntity = refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    public static String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
