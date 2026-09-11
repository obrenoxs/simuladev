package io.github.obrenoxs.simuladev.user.mapper;

import io.github.obrenoxs.simuladev.user.dto.request.UserRequest;
import io.github.obrenoxs.simuladev.user.dto.response.UserResponse;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "stack", constant = "Java + Spring")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "currentLevel", constant = "ESTAGIARIO")
    @Mapping(target = "levelPercentage", constant = "0")
    User toEntity(UserRequest dto, String encodedPassword);

    UserResponse toResponse(User user);
}
