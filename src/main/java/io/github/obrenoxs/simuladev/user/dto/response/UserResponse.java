package io.github.obrenoxs.simuladev.user.dto.response;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email
) {}
