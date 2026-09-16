package io.github.obrenoxs.simuladev.concept.controller;

import io.github.obrenoxs.simuladev.concept.dto.response.ConceptResponse;
import io.github.obrenoxs.simuladev.concept.service.ConceptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/concepts")
public class ConceptController {

    private final ConceptService conceptService;

    public ConceptController(ConceptService conceptService) {
        this.conceptService = conceptService;
    }

    @GetMapping
    public ResponseEntity<List<ConceptResponse>> findAll() {
        List<ConceptResponse> listResponse = conceptService.findAll();
        return ResponseEntity.ok().body(listResponse);
    }
}
