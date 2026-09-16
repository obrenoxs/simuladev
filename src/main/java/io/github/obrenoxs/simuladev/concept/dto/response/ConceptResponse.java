package io.github.obrenoxs.simuladev.concept.dto.response;

import java.util.UUID;

public record ConceptResponse(
        UUID id,
        String stack,
        String category,
        String conceptName,
        Integer weight,
        String targetLevel
) {}
