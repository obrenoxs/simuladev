package io.github.obrenoxs.simuladev.companylink.controller;

import io.github.obrenoxs.simuladev.companylink.dto.request.CompanyLinkRequest;
import io.github.obrenoxs.simuladev.companylink.dto.response.CompanyLinkResponse;
import io.github.obrenoxs.simuladev.companylink.service.CompanyLinkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/company-links")
public class CompanyLinkController {

    private final CompanyLinkService companyLinkService;

    public CompanyLinkController(CompanyLinkService companyLinkService) {
        this.companyLinkService = companyLinkService;
    }

    @PostMapping
    public ResponseEntity<CompanyLinkResponse> create(@Valid @RequestBody CompanyLinkRequest request, Authentication authentication) {
        Object object = authentication.getPrincipal();
        UUID id = UUID.fromString(object.toString());

        CompanyLinkResponse companyLinkResponse = companyLinkService.create(request, id);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(companyLinkResponse.id()).toUri();

        return ResponseEntity.created(uri).body(companyLinkResponse);
    }

    @GetMapping
    public ResponseEntity<List<CompanyLinkResponse>> findAllByUser(Authentication authentication) {
        Object object = authentication.getPrincipal();
        UUID userId = UUID.fromString(object.toString());

        List<CompanyLinkResponse> list = companyLinkService.findAllByUser(userId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CompanyLinkResponse> findById(@PathVariable UUID id, Authentication authentication) {
        Object object = authentication.getPrincipal();
        UUID userId = UUID.fromString(object.toString());

        CompanyLinkResponse companyLinkResponse = companyLinkService.findById(id, userId);
        return ResponseEntity.ok().body(companyLinkResponse);
    }
}
