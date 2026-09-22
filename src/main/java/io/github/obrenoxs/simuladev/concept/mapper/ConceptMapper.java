package io.github.obrenoxs.simuladev.concept.mapper;

import io.github.obrenoxs.simuladev.concept.dto.response.ConceptResponse;
import io.github.obrenoxs.simuladev.concept.dto.response.ConceptSummaryResponse;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConceptMapper {

    ConceptResponse toResponse(Concept concept);

    ConceptSummaryResponse toSummary(Concept concept);
}
