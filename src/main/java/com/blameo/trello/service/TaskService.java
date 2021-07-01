package com.blameo.trello.service;

import com.blameo.trello.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    public List<Task> getAll();
}
