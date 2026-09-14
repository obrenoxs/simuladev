package io.github.obrenoxs.simuladev.companytype.service;

import io.github.obrenoxs.simuladev.companytype.dto.response.CompanyTypeResponse;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.companytype.mapper.CompanyTypeMapper;
import io.github.obrenoxs.simuladev.companytype.repository.CompanyTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyTypeService {

    private final CompanyTypeRepository companyTypeRepository;
    private final CompanyTypeMapper companyTypeMapper;

    public CompanyTypeService(CompanyTypeRepository companyTypeRepository, CompanyTypeMapper companyTypeMapper) {
        this.companyTypeRepository = companyTypeRepository;
        this.companyTypeMapper = companyTypeMapper;
    }

    @Transactional(readOnly = true)
    public List<CompanyTypeResponse> findAll() {
        List<CompanyType> list = companyTypeRepository.findAll();
        List<CompanyTypeResponse> listResponse = list.stream().map(companyTypeMapper::toResponse).toList();

        return listResponse;
    }
}
