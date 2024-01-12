package com.example.accommodationbookingapp.service.user;

import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
