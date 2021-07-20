package com.blameo.trello.controller;


import com.blameo.trello.model.Comment;
import com.blameo.trello.model.Task;
import com.blameo.trello.model.User;
import com.blameo.trello.model.dto.CommentDto;
import com.blameo.trello.repository.*;
import com.blameo.trello.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestParam("taskId") Long taskId,
                                           @RequestParam("content") String content,
                                           Authentication authentication) {
        if (taskRepository.findById(taskId).isPresent()) {
            User user = userRepository.findByUsername(authentication.getName());
            Comment comment = new Comment();
            comment.setCreateBy(user.getId());
            comment.setContent(content);
            comment.setTaskId(taskId);
            commentRepository.save(comment);
            return ResponseEntity.ok("Create success");
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll(@RequestParam("taskId") Long taskId, Authentication authentication) {
        if (taskRepository.findById(taskId).isPresent()) {
            List<CommentDto> list = commentService.getAllComment(taskId, authentication);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-comment")
    public ResponseEntity<?> updateComment(@RequestParam("commentId") Long commentId, @RequestParam("content") String content) {

        if (commentRepository.findById(commentId).isPresent()) {
            Comment comment = commentRepository.findById(commentId).get();
            comment.setContent(content);
            commentRepository.save(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    public ResponseEntity<?> update(@RequestParam("taskId"))
}
