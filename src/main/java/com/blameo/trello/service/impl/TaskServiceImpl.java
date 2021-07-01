package com.blameo.trello.service.impl;

import com.blameo.trello.model.Task;
import com.blameo.trello.repository.TaskRepository;
import com.blameo.trello.service.TaskService;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAll() {
        return this.taskRepository.findAll();
    }
}
