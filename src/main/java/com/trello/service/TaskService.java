package com.trello.service;

import com.trello.model.Task;
import com.trello.model.TaskUser;
import com.trello.model.User;
import com.trello.model.WorkList;
import com.trello.model.dto.SearchUserDto;
import com.trello.repository.TaskRepository;
import com.trello.repository.TaskUserRepository;
import com.trello.repository.UserRepository;
import com.trello.repository.WorkListRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskUserRepository taskUserRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkListRepository workListRepository;

    @Autowired
    BoardService boardService;

    public void createTask(Long workListId, String title, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        Task task = new Task();
        task.setDisPlayOrder(taskRepository.countDisplayOrder(workListId));
        task.setTitle(title);
        WorkList workList = workListRepository.getById(workListId);
        task.setWorkList(workList);
        task.setCreateBy(user.getUserId());
        taskRepository.save(task);
    }

    public String addPersonToTask(Long taskId, Long userId) {
        if (taskRepository.findById(taskId).isPresent() && userRepository.findById(userId).isPresent()) {
            Task task = taskRepository.getById(taskId);
            User user = userRepository.getById(userId);
            TaskUser taskUser = new TaskUser();
            taskUser.setUser(user);
            taskUser.setTask(task);
            if (!taskUserRepository.findByUserAndTask(user, task).isPresent()) {
                taskUserRepository.save(taskUser);
                return "Add person to task success";
            }
            return "User is exist in task";
        }
        return "Task or user is not exist";
    }


    public String deletePersonToTask(Long taskId, Long userId) {
        if (taskRepository.findById(taskId).isPresent() && userRepository.findById(userId).isPresent()) {
            Task task = taskRepository.getById(taskId);
            User user = userRepository.getById(userId);
            TaskUser taskUser = new TaskUser();
            taskUser.setUser(user);
            taskUser.setTask(task);
            if (taskUserRepository.findByUserAndTask(user, task).isPresent()) {
                TaskUser tu = taskUserRepository.findByUserAndTask(user, task).get();
                taskUserRepository.deleteById(tu.getId());
                return "Delete person to task success";
            } else return "User is not exist in task";
        }
        return "Task or user is not exist";
    }

    public void updateDisplayOrder(Long removeId, Long removedIndex, Long addId, Long addedIndex) {
        List<Task> listAdd = taskRepository.getAllByWorkListId(addId);
        Task taskRemove = taskRepository.getByDisPlayOrder(removedIndex, removeId);
        WorkList workList = workListRepository.getById(addId);
        taskRemove.setWorkList(workList);

        listAdd.forEach(x -> {
            if (x.getDisPlayOrder() >= addedIndex) {
                x.setDisPlayOrder(x.getDisPlayOrder() + 1);
                taskRepository.save(x);
            }
        });
        taskRemove.setWorkList(workList);
        taskRemove.setDisPlayOrder(addedIndex);
        taskRepository.save(taskRemove);

        List<Task> listRemove = taskRepository.getAllByWorkListId(removeId);
        listRemove.forEach(x -> {
            if (x.getDisPlayOrder() > removedIndex) {
                x.setDisPlayOrder(x.getDisPlayOrder() - 1);
                taskRepository.save(x);
            }
        });
    }

    public List<SearchUserDto> getAllPersonInTask(Long taskId) {
        List<SearchUserDto> userDtos = new ArrayList<>();
        List<TaskUser> taskUsers = taskUserRepository.findByTask(taskRepository.getById(taskId));

        taskUsers.forEach(x -> {
            SearchUserDto userDto = new SearchUserDto();
            BeanUtils.copyProperties(x.getUser(), userDto);
            userDtos.add(userDto);
        });
        return userDtos;
    }

    public List<SearchUserDto> getAllPersonToAddToTask(Long taskId) {
        List<SearchUserDto> userDtos = new ArrayList<>();
        Task task = taskRepository.getById(taskId);
        List<SearchUserDto> listInBoard = boardService.getUserInBoard(task.getWorkList().getBoard().getBoardId());
        List<SearchUserDto> listInTask = getAllPersonInTask(taskId);

        for (int i = 0; i < listInBoard.size(); i++) {
            boolean check = true;
            for (int j = 0; j < listInTask.size(); j++) {
                if (listInBoard.get(i).getId() == listInTask.get(j).getId()) {
                    check = false;
                    break;
                }
            }
            if (check) {
                userDtos.add(listInBoard.get(i));
            }
        }
        return userDtos;
    }
}
