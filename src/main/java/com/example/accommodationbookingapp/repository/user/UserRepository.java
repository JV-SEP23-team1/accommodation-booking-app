package com.example.accommodationbookingapp.repository.user;

import com.example.accommodationbookingapp.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserByChatId(Long chatId);

    Optional<User> findByChatId(Long chatId);
}
