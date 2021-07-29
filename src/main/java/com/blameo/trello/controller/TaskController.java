package com.blameo.trello.controller;

import com.blameo.trello.model.dto.SearchUserDto;
import com.blameo.trello.model.request.TaskRequest;
import com.blameo.trello.repository.WorkListRepository;
import com.blameo.trello.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/task")
public class TaskController {

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest, Authentication authentication) {
        if (workListRepository.findById(taskRequest.getWorkListId()).isPresent()) {
            taskService.createTask(taskRequest.getWorkListId(), taskRequest.getTitle(), authentication);
            return ResponseEntity.ok("Create success");
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/add-person-to-task")
    public ResponseEntity<?> addPersonToTask(@RequestBody TaskRequest taskRequest) {
        String message = taskService.addPersonToTask(taskRequest.getTaskId(), taskRequest.getUserId());
        return ResponseEntity.ok(message);
    }

    @PutMapping("/delete-person-to-task")
    public ResponseEntity<?> deletePersonToTask(@RequestBody TaskRequest taskRequest) {
        String message = taskService.deletePersonToTask(taskRequest.getTaskId(), taskRequest.getUserId());
        return ResponseEntity.ok(message);
    }

    @PutMapping("/update-display-order")
    public ResponseEntity<?> updateDisplayOrder(@RequestBody TaskRequest taskRequest) {
        taskService.updateDisplayOrder(taskRequest.getRemoveId(), taskRequest.getRemovedIndex(), taskRequest.getAddId(), taskRequest.getAddedIndex());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/get-all-person-in-task")
    public ResponseEntity<?> getAllPersonInTask(@RequestParam("taskId") Long taskId) {
        List<SearchUserDto> user = taskService.getAllPersonInTask(taskId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
