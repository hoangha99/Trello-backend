package com.blameo.trello.controller;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.User;
import com.blameo.trello.model.WorkList;
import com.blameo.trello.repository.BoardRepository;
import com.blameo.trello.repository.UserRepository;
import com.blameo.trello.repository.WorkListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/workList")
public class WorkListController {

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam("boardId") Long boardId, @RequestParam("title") String title, Authentication authentication) {
        Optional<Board> board = boardRepository.findById(boardId);
        if (board.isPresent()) {
            User user = userRepository.findByUsername(authentication.getName());
            WorkList workList = new WorkList();
            workList.setCreateBy(user.getId());
            workList.setBoard(board.get());
            workList.setTitle(title);
            workList.setDisplayOrder(workListRepository.countDisplayOrder(boardId) + 1l);
            workListRepository.save(workList);
            return new ResponseEntity<>(workList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/getAll")
//    public ResponseEntity<List<WorkList>> getAll(@RequestParam("boardId") Long boarId) {
//        try {
//            List<WorkList> workLists = workListRepository.findByBoard(boardRepository.getById(boarId));
//            workLists = workLists.stream()
//                    .sorted(Comparator.comparingLong(WorkList::getDisplayOrder))
//                    .collect(Collectors.toList());
//
//            return new ResponseEntity<>(workLists, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("workListId") Long workListId) {
        Optional<WorkList> workList = workListRepository.findById(workListId);
        if (workList.isPresent()) {
            workListRepository.deleteById(workListId);
            return ResponseEntity.ok("OK");
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
