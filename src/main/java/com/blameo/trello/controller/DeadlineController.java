package com.blameo.trello.controller;


import com.blameo.trello.model.Deadline;
import com.blameo.trello.model.dto.CommentDto;
import com.blameo.trello.model.request.DeadlineRequest;
import com.blameo.trello.repository.DeadlineRepository;
import com.blameo.trello.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/deadline")
public class DeadlineController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    DeadlineRepository deadlineRepository;

    @GetMapping("/get-deadline")
    public ResponseEntity<?> getDeadline(@RequestParam("taskId") Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {

            Deadline deadline = deadlineRepository.getByTaskId(taskId);
            return new ResponseEntity<>(deadline, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DeadlineRequest deadlineRequest) {
        if (taskRepository.findById(deadlineRequest.getTaskId()).isPresent()) {

            Deadline deadline = new Deadline();
            deadline.setStartDate(deadlineRequest.getStartDate());
            deadline.setEndDate(deadlineRequest.getEndDate());
            deadline.setTaskId(deadline.getTaskId());
            deadlineRepository.save(deadline);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDeadline(@RequestBody DeadlineRequest deadlineRequest) {
        if (taskRepository.findById(deadlineRequest.getTaskId()).isPresent()) {
            Deadline deadline = deadlineRepository.getByTaskId(deadlineRequest.getTaskId());
            deadline.setStartDate(deadlineRequest.getStartDate());
            deadline.setEndDate(deadlineRequest.getEndDate());
            deadlineRepository.save(deadline);
            return new ResponseEntity<>(deadline, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
