package com.blameo.trello.repository;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository  extends JpaRepository<Board, Long> {

    @Query("select board FROM Board board " +
            "inner JOIN board.users bu where bu in (:boards) ")
    List<Board> getAllBoardByEmailUser();
}
