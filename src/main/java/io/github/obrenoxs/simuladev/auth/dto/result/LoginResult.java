package io.github.obrenoxs.simuladev.auth.dto.result;

import io.github.obrenoxs.simuladev.auth.dto.response.LoginResponse;

public record LoginResult(LoginResponse response, String refreshToken) {}
