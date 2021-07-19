package com.blameo.trello.controller;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.BoardUser;
import com.blameo.trello.model.User;
import com.blameo.trello.repository.BoardRepository;
import com.blameo.trello.repository.BoardUserRepository;
import com.blameo.trello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/board")
public class BoardController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardUserRepository boardUserRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestParam("title") String title, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Board board = new Board();
        board.setTitle(title);
        board.setCreateBy(user.getId());
        BoardUser boardUser = new BoardUser();
        boardUser.setUser(user);
        boardUser.setBoard(board);
        boardRepository.save(board);
        boardUserRepository.save(boardUser);
        return ResponseEntity.ok("Create success");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteBoard(@RequestParam("id") Long id) {
        try {
            Board board = boardRepository.getById(id);
            board.setIsHide(true);
            boardRepository.save(board);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Board>> getAll(Authentication authentication) {
        try {
            List<BoardUser> boardUserList =
                    boardUserRepository.findByUser(userRepository.findByUsername(authentication.getName()));
            List<Board> boardList = new ArrayList<>();
            boardUserList.forEach(bu -> {
                boardList.add(boardRepository.findByBoardUsers(bu));
            });
            return new ResponseEntity<>(boardList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/addUserToBoard")
    public ResponseEntity<?> addUserToBoard(@RequestParam("userId") Long userId, @RequestParam("boardId") Long boardId) {
        try {
            User user = userRepository.getById(userId);
            Board board = boardRepository.getById(boardId);
            BoardUser boardUser = new BoardUser();
            boardUser.setUser(user);
            boardUser.setBoard(board);
            boardUserRepository.save(boardUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
