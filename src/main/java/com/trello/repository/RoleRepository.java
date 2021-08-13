package com.trello.repository;

import com.trello.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select r.name from Role r where r.id = :id")
    String findbyRole(Long id);
}
