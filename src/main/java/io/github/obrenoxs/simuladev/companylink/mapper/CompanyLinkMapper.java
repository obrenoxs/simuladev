package io.github.obrenoxs.simuladev.companylink.mapper;

import io.github.obrenoxs.simuladev.companylink.dto.request.CompanyLinkRequest;
import io.github.obrenoxs.simuladev.companylink.dto.response.CompanyLinkResponse;
import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.companytype.mapper.CompanyTypeMapper;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CompanyTypeMapper.class)
public interface CompanyLinkMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    CompanyLink toEntity(CompanyLinkRequest request, User user, CompanyType companyType);

    CompanyLinkResponse toResponse(CompanyLink companyLink);
}
