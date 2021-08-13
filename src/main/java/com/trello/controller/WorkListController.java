package com.trello.controller;

import com.trello.model.Board;
import com.trello.model.User;
import com.trello.model.WorkList;
import com.trello.model.request.WorkListRequest;
import com.trello.repository.BoardRepository;
import com.trello.repository.UserRepository;
import com.trello.repository.WorkListRepository;
import com.trello.service.WorkListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/work-list")
public class WorkListController {

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    WorkListService workListService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody WorkListRequest workListRequest, Authentication authentication) {
        Optional<Board> board = boardRepository.findById(workListRequest.getBoardId());
        if (board.isPresent()) {
            User user = userRepository.findByUsername(authentication.getName());
            WorkList workList = new WorkList();
            workList.setCreateBy(user.getUserId());
            workList.setBoard(board.get());
            workList.setTitle(workListRequest.getTitle());
            workList.setDisplayOrder(workListRepository.countDisplayOrder(workListRequest.getBoardId()));
            workListRepository.save(workList);
            return new ResponseEntity<>(workList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<WorkList>> getAll(@RequestParam("boardId") Long boarId) {
        try {
            List<WorkList> workLists = workListService.getAll(boarId);
            return new ResponseEntity<>(workLists, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("workListId") Long workListId) {
        if (workListRepository.findById(workListId).isPresent()) {
            workListService.deleteWorkList(workListId);
            return ResponseEntity.ok("Deleted");
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rename")
    public ResponseEntity<?> rename(@RequestBody WorkListRequest workListRequest) {

        if (workListRepository.findById(workListRequest.getWorkListId()).isPresent()) {
            WorkList workList = workListRepository.getById(workListRequest.getWorkListId());

            workList.setTitle(workListRequest.getTitle());
            workListRepository.save(workList);
            return new ResponseEntity<>(workList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-display-order")
    public ResponseEntity<?> updateDisplayOrder(@RequestBody WorkListRequest workListRequest) {

        if (workListRequest.getAddedIndex() != workListRequest.getRemoveIndex()) {
            workListService.updateDisplayOrder(workListRequest.getAddedIndex() , workListRequest.getRemoveIndex(), workListRequest.getBoardId());
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
