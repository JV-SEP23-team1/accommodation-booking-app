package com.example.accommodationbookingapp.service.user.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.mapper.UserMapper;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static Long userId;
    private static String oldPassword;
    private static String newPassword;
    private static UserUpdateRequestDto requestDto;
    private static UserResponseDto responseDto;
    private static User existingUser;
    private static User updatedUser;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeAll
    static void beforeAll() {
        userId = 1L;
        oldPassword = "$2a$10$kmYQ3CgmYPzWNsK9q8oLF.VCoXCDeXqY2suHL2Z6GdnN18CtLkjTO";
        newPassword = "$2a$10$6B/OEUMjOK1iH.To/CISqun82xxhDc/lmsDjox9oJlK4JEeZD0Nu2";

        requestDto = new UserUpdateRequestDto();
        requestDto.setOldPassword(oldPassword);
        requestDto.setNewPassword(newPassword);
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setEmail("john@gmail.com");

        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Bob");
        existingUser.setLastName("Smith");
        existingUser.setEmail("bob@gmail.com");
        existingUser.setPassword(oldPassword);

        updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(requestDto.getFirstName());
        updatedUser.setLastName(requestDto.getLastName());
        updatedUser.setEmail(requestDto.getEmail());
        updatedUser.setPassword(newPassword);

        responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setFirstName("John");
        responseDto.setLastName("Doe");
        responseDto.setEmail("john@gmail.com");
    }

    @Test
    public void updateFullCurrentUser_ValidUserUpdate_Successful() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(eq(oldPassword), anyString())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto expectedDto = responseDto;

        UserResponseDto actualDto = userServiceImpl.updateFullCurrentUser(userId, requestDto);

        Assertions.assertNotNull(actualDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }
}
