package com.example.accommodationbookingapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbookingapp.dto.user.UserResponseDto;
import com.example.accommodationbookingapp.dto.user.UserUpdateRequestDto;
import com.example.accommodationbookingapp.dto.user.UserWithRolesResponseDto;
import com.example.accommodationbookingapp.model.Role;
import com.example.accommodationbookingapp.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    protected static MockMvc mockMvc;

    private static UserWithRolesResponseDto userWithRolesResponseDto;
    private static UserUpdateRequestDto userUpdateRequestDto;
    private static UserResponseDto userResponseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        userWithRolesResponseDto = new UserWithRolesResponseDto();
        userWithRolesResponseDto.setId(1L);
        userWithRolesResponseDto.setEmail("john@example.com");
        userWithRolesResponseDto.setFirstName("John");
        userWithRolesResponseDto.setLastName("Doe");
        userWithRolesResponseDto.setRoleNames(Set.of(Role.RoleName.USER));

        userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setFirstName("John");
        userUpdateRequestDto.setLastName("Doe");
        userUpdateRequestDto.setEmail("john@gmail.com");
        userUpdateRequestDto.setOldPassword("12345678");
        userUpdateRequestDto.setNewPassword("111111");

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setFirstName(userUpdateRequestDto.getFirstName());
        userResponseDto.setLastName(userUpdateRequestDto.getLastName());
        userResponseDto.setEmail(userUpdateRequestDto.getEmail());
    }

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.USER);

        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setPassword("$2a$10$kmYQ3CgmYPzWNsK9q8oLF.VCoXCDeXqY2suHL2Z6GdnN18CtLkjTO");
        user.setRoles(Set.of(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    public void getUser_ValidUser_Successful() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/users/me").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserWithRolesResponseDto expected = userWithRolesResponseDto;

        UserWithRolesResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserWithRolesResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void updateFullUserProfile_ValidUpdate_Successful() throws Exception {
        MvcResult result = mockMvc.perform(
                put("/users/me")
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto expected = userResponseDto;

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }
}
