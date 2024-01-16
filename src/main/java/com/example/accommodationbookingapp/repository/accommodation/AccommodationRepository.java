package com.example.accommodationbookingapp.repository.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
