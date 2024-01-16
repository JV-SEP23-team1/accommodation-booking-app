package com.example.accommodationbookingapp.service.user;

import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserRoleUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import java.util.Map;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    UserWithRolesResponseDto findCurrentUser(Long id);

    UserWithRolesResponseDto updateUserRole(Long id, UserRoleUpdateRequestDto requestDto);

    UserResponseDto updateFullCurrentUser(Long id, UserUpdateRequestDto requestDto);

    UserResponseDto updatePartOfCurrentUser(Long id, Map<String, Object> updates);
}
