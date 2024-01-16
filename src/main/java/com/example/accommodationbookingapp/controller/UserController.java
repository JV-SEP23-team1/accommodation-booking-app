package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserRoleUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User manager", description = "Endpoint to manage users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    @Operation(
            summary = "Get information about user",
            description = "Retrieves the profile information for the currently logged-in user"
    )
    public UserWithRolesResponseDto getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.findCurrentUser(user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(
            summary = "Update user role",
            description = "Enables users to update their roles, providing role-based access"
    )
    public UserWithRolesResponseDto updateUserRole(
            @PathVariable Long id,
            @RequestBody @Valid UserRoleUpdateRequestDto requestDto
    ) {
        return userService.updateUserRole(id, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/me")
    @Operation(
            summary = "Update user profile",
            description = " Allows users to update their profile information"
    )
    public UserResponseDto updateFullUserProfile(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateFullCurrentUser(user.getId(), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/me")
    @Operation(
            summary = "Update user profile",
            description = " Allows users to update their profile information"
    )
    public UserResponseDto updatePartOfUserProfile(
            Authentication authentication,
            @RequestBody Map<String, Object> updates
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.updatePartOfCurrentUser(user.getId(), updates);
    }
}
