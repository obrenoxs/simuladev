package io.github.obrenoxs.simuladev.auth.refresh.repository;

import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    List<RefreshToken> findAllByUserId(UUID userId);

    Optional<RefreshToken> findByHashToken(String hash);
}
