package com.example.accommodationbookingapp.controller;

import com.example.accommodationbookingapp.dto.user.UserLoginRequestDto;
import com.example.accommodationbookingapp.dto.user.UserLoginResponseDto;
import com.example.accommodationbookingapp.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.exception.AuthenticationException;
import com.example.accommodationbookingapp.exception.RegistrationException;
import com.example.accommodationbookingapp.security.AuthenticationService;
import com.example.accommodationbookingapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication manager", description = "Endpoint to authenticate users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Register a new user")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login using existing credentials",
            description = "Login using existing credentials"
    )
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto)
            throws AuthenticationException {
        return authenticationService.authenticate(requestDto);
    }
}
