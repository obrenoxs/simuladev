package io.github.obrenoxs.simuladev.concept.dto.response;

import io.github.obrenoxs.simuladev.concept.enums.TaskType;

import java.util.Set;
import java.util.UUID;

public record ConceptResponse(
        UUID id,
        String stack,
        String category,
        String conceptName,
        Integer weight,
        String targetLevel,
        Set<ConceptSummaryResponse> prerequisites,
        Set<TaskType> taskTypes
) {}
