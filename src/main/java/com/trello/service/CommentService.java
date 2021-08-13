package com.trello.service;


import com.trello.model.Comment;
import com.trello.model.User;
import com.trello.model.dto.CommentDto;
import com.trello.repository.CommentRepository;
import com.trello.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;


    public List<CommentDto> getAllComment(Long taskId, Authentication authentication) {
        List<CommentDto> list = new ArrayList<>();
        List<Comment> commentList = commentRepository.findByTaskId(taskId);
        commentList.forEach(x ->{
            User user = userRepository.findByUsername(authentication.getName());
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(x, commentDto);
            String fullName = userRepository.getById(x.getCreateBy()).getFullName();
            commentDto.setFullName(fullName);
            if(x.getCreateBy()==user.getUserId()){
                commentDto.setEdit(true);
            }
            list.add(commentDto);
        });
        return list;
    }
}
