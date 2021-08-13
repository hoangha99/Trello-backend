package com.trello.service;

import com.trello.model.Board;
import com.trello.model.BoardUser;
import com.trello.model.User;
import com.trello.model.dto.SearchUserDto;
import com.trello.repository.BoardRepository;
import com.trello.repository.BoardUserRepository;
import com.trello.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        board.setCreateBy(user.getUserId());
        BoardUser boardUser = new BoardUser();
        boardUser.setUser(user);
        boardUser.setBoard(board);
        boardRepository.save(board);
        boardUserRepository.save(boardUser);
    }

    public List<SearchUserDto> getUserInBoard(Long boardId){
        Board board =boardRepository.getById(boardId);
        List<BoardUser> boardUserList=boardUserRepository.findByBoard(board);
        List<SearchUserDto> searchUserDtos = new ArrayList<>();
        boardUserList.forEach(x->{
            SearchUserDto searchUserDto = new SearchUserDto();
            BeanUtils.copyProperties(x.getUser(), searchUserDto);
            searchUserDto.setId(x.getUser().getUserId());
            searchUserDtos.add(searchUserDto);
        });
        return searchUserDtos;
    }
}
