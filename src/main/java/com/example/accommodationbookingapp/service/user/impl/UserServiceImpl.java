package com.example.accommodationbookingapp.service.user.impl;

import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserRoleUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import com.example.accommodationbookingapp.exception.EntityNotFoundException;
import com.example.accommodationbookingapp.exception.InvalidOldPasswordException;
import com.example.accommodationbookingapp.exception.RegistrationException;
import com.example.accommodationbookingapp.mapper.UserMapper;
import com.example.accommodationbookingapp.model.Role;
import com.example.accommodationbookingapp.model.User;
import com.example.accommodationbookingapp.repository.role.RoleRepository;
import com.example.accommodationbookingapp.repository.user.UserRepository;
import com.example.accommodationbookingapp.service.user.UserService;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String REGISTER_ERROR_MESSAGE = "Can't register a new user with email: ";
    private static final String FIND_CURRENT_USER_ERROR_MESSAGE = "Can't find user by id: ";
    private static final String OLD_PASSWORD_MATCHING_ERROR = "Old password is incorrect";
    private static final String NEW_PASSWORD_MUST_BE_SENT_ERROR =
            "If oldPassword is sent, newPassword must also be sent";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(REGISTER_ERROR_MESSAGE + requestDto.getEmail()
            );
        }
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER);
        User user = userMapper.toModel(requestDto);
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserWithRolesResponseDto findCurrentUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(FIND_CURRENT_USER_ERROR_MESSAGE + id)
        );
        return userMapper.toDtoWithRoles(user);
    }

    @Override
    public UserWithRolesResponseDto updateUserRole(Long id, UserRoleUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(FIND_CURRENT_USER_ERROR_MESSAGE + id)
                );

        Set<Role> existingRoles = roleRepository.findByRoleNameIn(requestDto.getRoleNames());
        user.setRoles(existingRoles);

        User updatedUser = userRepository.save(user);
        return userMapper.toDtoWithRoles(updatedUser);
    }

    @Override
    public UserResponseDto updateFullCurrentUser(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(FIND_CURRENT_USER_ERROR_MESSAGE + id)
                );

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException(OLD_PASSWORD_MATCHING_ERROR);
        }

        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public UserResponseDto updatePartOfCurrentUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(FIND_CURRENT_USER_ERROR_MESSAGE + id)
                );

        if (updates.containsKey("oldPassword")) {
            if (!updates.containsKey("newPassword")) {
                throw new InvalidOldPasswordException(NEW_PASSWORD_MUST_BE_SENT_ERROR);
            }

            String oldPassword = (String) updates.get("oldPassword");
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new InvalidOldPasswordException(OLD_PASSWORD_MATCHING_ERROR);
            }

            String newPassword = (String) updates.get("newPassword");
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }

        if (updates.containsKey("lastName")) {
            user.setLastName((String) updates.get("lastName"));
        }

        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }
}
