package com.blameo.trello.controller;

import com.blameo.trello.model.Task;
import com.blameo.trello.model.User;
import com.blameo.trello.repository.BoardRepository;
import com.blameo.trello.repository.TaskRepository;
import com.blameo.trello.repository.UserRepository;
import com.blameo.trello.repository.WorkListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/task")
public class TaskController {

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestParam("workListId") Long workListId, @RequestParam("title") String title, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        if (workListRepository.findById(workListId).isPresent()) {
            Task task = new Task();
            task.setDisPlayOrder(taskRepository.countDisplayOrder(workListId) + 1l);
            task.setCreateBy(user.getId());
            task.setTitle(title);
            task.setWorkList(workListRepository.getById(workListId));
            taskRepository.save(task);
            return ResponseEntity.ok("Create success");
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/add-person-to-task")
    public ResponseEntity<?> addPersonToTask(@RequestParam("taskId") Long taskId, @RequestParam("userId") Long userId) {
        if (taskRepository.findById(taskId).isPresent() && userRepository.findById(userId).isPresent()) {
            Task task = taskRepository.findById(taskId).get();
            task.setPersonWorkId(userId);
            taskRepository.save(task);
            return ResponseEntity.ok("Add person to task success");
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/delete-person-to-task")
    public ResponseEntity<?> deletePersonToTask(@RequestParam("taskId") Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            Task task = taskRepository.findById(taskId).get();
            task.setPersonWorkId(null);
            taskRepository.save(task);
            return ResponseEntity.ok("Delete person to task success");
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
