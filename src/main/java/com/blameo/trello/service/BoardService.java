package com.blameo.trello.service;

import com.blameo.trello.model.Board;
import com.blameo.trello.model.BoardUser;
import com.blameo.trello.model.User;
import com.blameo.trello.repository.BoardRepository;
import com.blameo.trello.repository.BoardUserRepository;
import com.blameo.trello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardUserRepository boardUserRepository;

    public void createBoard(String title, Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName());
        Board board = new Board();
        board.setTitle(title);
        board.setCreateBy(user.getId());
        BoardUser boardUser = new BoardUser();
        boardUser.setUser(user);
        boardUser.setBoard(board);
        boardRepository.save(board);
        boardUserRepository.save(boardUser);
    }
}
