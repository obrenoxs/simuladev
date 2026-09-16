package io.github.obrenoxs.simuladev.concept.service;

import io.github.obrenoxs.simuladev.concept.dto.response.ConceptResponse;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.concept.mapper.ConceptMapper;
import io.github.obrenoxs.simuladev.concept.repository.ConceptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConceptService {

    private final ConceptRepository conceptRepository;
    private final ConceptMapper conceptMapper;

    public ConceptService(ConceptRepository conceptRepository, ConceptMapper conceptMapper) {
        this.conceptRepository = conceptRepository;
        this.conceptMapper = conceptMapper;
    }

    @Transactional(readOnly = true)
    public List<ConceptResponse> findAll() {
        List<Concept> list = conceptRepository.findAll();
        List<ConceptResponse> listResponse = list.stream().map(conceptMapper::toResponse).toList();

        return listResponse;
    }
}
