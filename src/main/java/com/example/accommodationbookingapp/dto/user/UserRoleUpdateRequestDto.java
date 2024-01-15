package com.example.accommodationbookingapp.dto.user;

import com.example.accommodationbookingapp.model.Role;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

@Data
public class UserRoleUpdateRequestDto {
    @NotNull
    private Set<Role.RoleName> roleNames;
}
