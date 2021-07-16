package com.blameo.trello.controller;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.User;
import com.blameo.trello.repository.BoardRepository;
import com.blameo.trello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/board")
public class BoardController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@RequestParam("title") String title, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Board a = new Board();
        a.setTitle(title);
        a.add(user);
        a.setCreateBy(user.getId());
        boardRepository.save(a);
        return ResponseEntity.ok("Create success");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBoard(@PathVariable("id") Long id) {
        try {
            Board board = boardRepository.getById(id);
            board.setIsHide(true);
            boardRepository.save(board);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Board>> getAll(Authentication authentication) {
        try {
            List<Board> list = boardRepository.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
