package com.example.accommodationbookingapp.repository.role;

import com.example.accommodationbookingapp.model.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByRoleNameIn(Set<Role.RoleName> roleNames);
}
