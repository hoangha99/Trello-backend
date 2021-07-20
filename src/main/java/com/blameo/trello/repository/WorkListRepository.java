package com.blameo.trello.repository;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.WorkList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkListRepository extends JpaRepository<WorkList, Long> {
    List<WorkList> findByBoard(Board board);

    @Query("SELECT count(wl) FROM WorkList wl where wl.board.boardId = :boardId")
    Long countDisplayOrder(Long boardId);

//    List<WorkList> findAllByBoardId();
}
