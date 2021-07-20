package com.blameo.trello.repository;

import com.blameo.trello.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT count(task) FROM Task task where task.workList.workListId = :workListId")
    Long countDisplayOrder(Long workListId);
}
