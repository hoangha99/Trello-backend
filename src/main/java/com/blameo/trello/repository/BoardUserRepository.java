package com.blameo.trello.repository;

import com.blameo.trello.model.BoardUser;
import com.blameo.trello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    List<BoardUser> findByUser(User user);
}
