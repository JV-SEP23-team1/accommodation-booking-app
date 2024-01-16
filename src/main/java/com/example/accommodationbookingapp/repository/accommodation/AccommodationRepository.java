package com.example.accommodationbookingapp.repository.accommodation;

import com.example.accommodationbookingapp.model.Accommodation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("""
            select a from Accommodation a join fetch a.amenities
            """)
    Page<Accommodation> findAll(Pageable pageable);

    @Query("""
            select a from Accommodation a join fetch a.amenities where a.id = :id
            """)
    Optional<Accommodation> findById(Long id);
}
