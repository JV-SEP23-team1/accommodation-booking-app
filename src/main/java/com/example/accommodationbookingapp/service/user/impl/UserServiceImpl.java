package com.example.accommodationbookingapp.service.user.impl;

import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.exception.RegistrationException;
import com.example.accommodationbookingapp.mapper.UserMapper;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import com.example.accommodationbookingapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String REGISTER_ERROR_MESSAGE = "Can't register a new user with email: ";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(REGISTER_ERROR_MESSAGE + requestDto.getEmail()
            );
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
