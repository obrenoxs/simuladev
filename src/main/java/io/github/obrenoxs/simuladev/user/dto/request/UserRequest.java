package io.github.obrenoxs.simuladev.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 250)
    String name,

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 254)
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
    String password
) {}
