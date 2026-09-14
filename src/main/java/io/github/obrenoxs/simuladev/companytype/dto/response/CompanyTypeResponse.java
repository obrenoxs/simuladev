package io.github.obrenoxs.simuladev.companytype.dto.response;

import java.util.UUID;

public record CompanyTypeResponse(
        UUID id,
        String domainName,
        String description
) {}
