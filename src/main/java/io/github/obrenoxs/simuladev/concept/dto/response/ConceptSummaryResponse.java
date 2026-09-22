package io.github.obrenoxs.simuladev.concept.dto.response;

import java.util.UUID;

public record ConceptSummaryResponse(
        UUID id,
        String conceptName,
        String category
) {}
