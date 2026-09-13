package io.github.obrenoxs.simuladev.auth.dto.response;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String token,
        String name
) {}
