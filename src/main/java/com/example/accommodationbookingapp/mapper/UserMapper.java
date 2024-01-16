package com.example.accommodationbookingapp.mapper;

import com.example.accommodationbookingapp.config.MapperConfig;
import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import com.example.accommodationbookingapp.model.Role;
import com.example.accommodationbookingapp.model.User;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "roleNames", ignore = true)
    UserWithRolesResponseDto toDtoWithRoles(User user);

    @AfterMapping
    default void setRoleNames(
            @MappingTarget UserWithRolesResponseDto responseDto,
            User user
    ) {
        if (user.getRoles() != null) {
            responseDto.setRoleNames(user.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet()));
        }
    }

    User toModel(UserRegistrationRequestDto requestDto);
}
