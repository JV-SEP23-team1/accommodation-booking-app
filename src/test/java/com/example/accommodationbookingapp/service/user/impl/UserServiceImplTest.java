package com.example.accommodationbookingapp.service.user.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserRoleUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import com.example.accommodationbookingapp.mapper.UserMapper;
import com.example.accommodationbookingapp.model.Role;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.role.RoleRepository;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    private static UserUpdateRequestDto userUpdateRequestDto;
    private static UserResponseDto userResponseDto;
    private static UserRoleUpdateRequestDto userRoleUpdateRequestDto;
    private static UserWithRolesResponseDto userWithRolesResponseDto;
    private static User existingUser;
    private static User updatedUser;
    private static Set<Role> existingRoles;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
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

        userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setOldPassword(oldPassword);
        userUpdateRequestDto.setNewPassword(newPassword);
        userUpdateRequestDto.setFirstName("John");
        userUpdateRequestDto.setLastName("Doe");
        userUpdateRequestDto.setEmail("john@gmail.com");

        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Bob");
        existingUser.setLastName("Smith");
        existingUser.setEmail("bob@gmail.com");
        existingUser.setPassword(oldPassword);

        updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(userUpdateRequestDto.getFirstName());
        updatedUser.setLastName(userUpdateRequestDto.getLastName());
        updatedUser.setEmail(userUpdateRequestDto.getEmail());
        updatedUser.setPassword(newPassword);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(userId);
        userResponseDto.setFirstName("John");
        userResponseDto.setLastName("Doe");
        userResponseDto.setEmail("john@gmail.com");

        userRoleUpdateRequestDto = new UserRoleUpdateRequestDto();
        userRoleUpdateRequestDto.setRoleNames(Set.of(Role.RoleName.USER, Role.RoleName.ADMIN));

        existingRoles = new HashSet<>();
        existingRoles.add(new Role());
        existingRoles.add(new Role());
        existingUser.setRoles(existingRoles);

        updatedUser.setRoles(existingRoles);

        userWithRolesResponseDto = new UserWithRolesResponseDto();
        userWithRolesResponseDto.setId(userId);
        userWithRolesResponseDto.setFirstName("Bob");
        userWithRolesResponseDto.setLastName("Smith");
        userWithRolesResponseDto.setEmail("bob@gmail.com");
        userWithRolesResponseDto.setRoleNames(Set.of(Role.RoleName.USER, Role.RoleName.ADMIN));
    }

    @Test
    public void updateFullCurrentUser_ValidUserUpdate_Successful() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(eq(oldPassword), anyString())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(userResponseDto);

        UserResponseDto expectedDto = userResponseDto;

        UserResponseDto actualDto = userServiceImpl.updateFullCurrentUser(
                userId,
                userUpdateRequestDto
        );

        Assertions.assertNotNull(actualDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @Test
    public void updatePartOfCurrentUser_ValidUpdates_Successful() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "John");
        updates.put("lastName", "Doe");
        updates.put("email", "john@gmail.com");
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(userResponseDto);

        UserResponseDto expectedDto = userResponseDto;

        UserResponseDto actualDto = userServiceImpl.updatePartOfCurrentUser(userId, updates);

        Assertions.assertNotNull(actualDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }

    @Test
    public void updateUserRole_ValidUpdate_Successful() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findByRoleNameIn(userRoleUpdateRequestDto.getRoleNames()))
                .thenReturn(existingRoles);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDtoWithRoles(updatedUser)).thenReturn(userWithRolesResponseDto);

        UserWithRolesResponseDto expectedDto = userWithRolesResponseDto;

        UserWithRolesResponseDto actualDto = userServiceImpl.updateUserRole(
                userId,
                userRoleUpdateRequestDto
        );

        Assertions.assertNotNull(actualDto);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }
}
