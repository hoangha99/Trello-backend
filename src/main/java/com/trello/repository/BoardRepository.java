package com.trello.repository;

import com.trello.model.Board;
import com.trello.model.BoardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByBoardUsers(BoardUser boardUser);
}
