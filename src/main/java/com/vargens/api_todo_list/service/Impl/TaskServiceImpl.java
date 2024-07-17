package com.vargens.api_todo_list.service.Impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import com.vargens.api_todo_list.controller.dto.Task.CreateTaskDto;
import com.vargens.api_todo_list.controller.dto.Task.ResponseTaskDTO;
import com.vargens.api_todo_list.controller.dto.Task.TaskItemDTO;
import com.vargens.api_todo_list.entity.Role;
import com.vargens.api_todo_list.entity.Task;
import com.vargens.api_todo_list.entity.User;
import com.vargens.api_todo_list.repository.TaskRepository;
import com.vargens.api_todo_list.repository.UserRepository;
import com.vargens.api_todo_list.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createTask(CreateTaskDto taskDto, Optional<User> user) {
        var task = new Task();
        task.setUser(user.get());
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());

        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId, UUID tokenId) {
        var user = userRepository.findById(tokenId);
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRole()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || task.getUser().getId().equals(tokenId)) {
            taskRepository.deleteById(taskId);
        }
    }

    @Override
    public ResponseTaskDTO listTasks(int page, int pageSize) {
        var tasks = taskRepository.findAll(PageRequest.of(page, pageSize,
                Sort.Direction.DESC, "dueDate"))
                .map(task -> new TaskItemDTO(task.getId(), task.getTitle(),
                        task.getDescription()));

        return new ResponseTaskDTO(tasks.getContent(), page, pageSize,
                tasks.getTotalPages(),
                tasks.getTotalElements());

    }

}
