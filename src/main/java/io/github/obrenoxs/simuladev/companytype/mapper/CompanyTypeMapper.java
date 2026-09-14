package io.github.obrenoxs.simuladev.companytype.mapper;

import io.github.obrenoxs.simuladev.companytype.dto.response.CompanyTypeResponse;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyTypeMapper {

    CompanyTypeResponse toResponse(CompanyType companyType);
}
