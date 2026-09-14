package io.github.obrenoxs.simuladev.companytype.controller;

import io.github.obrenoxs.simuladev.companytype.dto.response.CompanyTypeResponse;
import io.github.obrenoxs.simuladev.companytype.service.CompanyTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/company-types")
public class CompanyTypeController {

    private final CompanyTypeService companyTypeService;

    public CompanyTypeController(CompanyTypeService companyTypeService) {
        this.companyTypeService = companyTypeService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyTypeResponse>> findAll() {
        List<CompanyTypeResponse> list = companyTypeService.findAll();
        return ResponseEntity.ok().body(list);
    }
}
