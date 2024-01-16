package com.example.accommodationbookingapp.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.accommodationbookingapp.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingapp.model.Accommodation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccommodationControllerTest {
    protected static MockMvc mockMvc;

    private static Long accommodationId;
    private static AccommodationResponseDto accommodationResponseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        accommodationId = 1L;

        accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(accommodationId);
        accommodationResponseDto.setLocation("Kyiv");
        accommodationResponseDto.setSize("Medium");
        accommodationResponseDto.setAmenitiesIds(List.of(1L, 2L, 3L));
        accommodationResponseDto.setDailyRate(new BigDecimal("2000.00"));
        accommodationResponseDto.setAvailability(2);
        accommodationResponseDto.setType(Accommodation.Type.APARTMENT);
    }

    @Test
    @Sql(
            scripts = {
                    "classpath:database/accommodations/add-accommodations.sql",
                    "classpath:database/accommodations_amenities/add-accommodations-amenities.sql"

            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    "classpath:database/accommodations_amenities/"
                            + "delete-accommodations-amenities.sql",
                    "classpath:database/accommodations/delete-accommodations.sql"
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getById_ValidAccommodation_Successful() throws Exception {
        Long id = accommodationId;
        MvcResult result = mockMvc.perform(
                        get("/accommodations/{id}", id).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        AccommodationResponseDto expectedDto = accommodationResponseDto;

        AccommodationResponseDto actualDto = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AccommodationResponseDto.class
        );

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id"));
    }
}
