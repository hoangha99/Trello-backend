package com.blameo.trello.repository;


import com.blameo.trello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select us from User us where us.email = :name")
    User findByUsername(String name);

    @Query("select count (us) from User us where us.email = :email")
    int checkExists(String email);

}