package io.github.obrenoxs.simuladev.companylink.dto.response;

import io.github.obrenoxs.simuladev.companytype.dto.response.CompanyTypeResponse;

import java.time.LocalDate;
import java.util.UUID;

public record CompanyLinkResponse(
        UUID id,
        String companyName,
        LocalDate startDate,
        boolean active,
        CompanyTypeResponse companyType
) {}
