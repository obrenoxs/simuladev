package io.github.obrenoxs.simuladev.companylink.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompanyLinkRequest(

        @NotNull(message = "Id é obrigatório")
        UUID companyTypeId,

        @NotBlank(message = "Nome é obrigatório")
        String companyName
) {}
