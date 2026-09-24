package io.github.obrenoxs.simuladev.auth.refresh.repository;

import io.github.obrenoxs.simuladev.auth.refresh.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
