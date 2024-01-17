package com.example.accommodationbookingapp.repository.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import com.example.accommodationbookingapp.model.Amenity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccommodationRepositoryTest {
    private static Accommodation accommodation;
    private static Amenity amenityPool;
    private static Amenity amenityGym;
    private static Amenity amenityFreeWiFi;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeAll
    static void beforeAll() {
        amenityPool = new Amenity();
        amenityPool.setId(1L);
        amenityPool.setName("Pool");

        amenityGym = new Amenity();
        amenityGym.setId(2L);
        amenityGym.setName("Gym");

        amenityFreeWiFi = new Amenity();
        amenityFreeWiFi.setId(3L);
        amenityFreeWiFi.setName("Free Wi-Fi");

        accommodation = new Accommodation();
        accommodation.setId(1L);
        accommodation.setLocation("Kyiv");
        accommodation.setSize("Medium");
        accommodation.setAmenities(List.of(amenityPool, amenityGym, amenityFreeWiFi));
        accommodation.setDailyRate(new BigDecimal("2000.00"));
        accommodation.setAvailability(2);
        accommodation.setType(Accommodation.Type.APARTMENT);
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
    public void findById_ValidAccommodation_ShouldReturnOptionalAccommodation() {
        Long accommodationId = 1L;

        Optional<Accommodation> result = accommodationRepository.findById(accommodationId);

        Assertions.assertTrue(result.isPresent());
        Accommodation actual = result.get();

        Accommodation expected = accommodation;

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }
}
