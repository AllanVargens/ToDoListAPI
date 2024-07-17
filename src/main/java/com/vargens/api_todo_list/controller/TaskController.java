package com.vargens.api_todo_list.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vargens.api_todo_list.controller.dto.Task.CreateTaskDto;
import com.vargens.api_todo_list.controller.dto.Task.ResponseTaskDTO;
import com.vargens.api_todo_list.repository.UserRepository;
import com.vargens.api_todo_list.service.Impl.TaskServiceImpl;

@RestController
public class TaskController {
    private final TaskServiceImpl taskServiceImpl;
    private final UserRepository userRepository;

    public TaskController(TaskServiceImpl taskServiceImpl, UserRepository userRepository) {
        this.taskServiceImpl = taskServiceImpl;
        this.userRepository = userRepository;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto taskDto,
            JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        taskServiceImpl.createTask(taskDto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskId, JwtAuthenticationToken token) {
        var token_id = UUID.fromString(token.getName());

        try {
            taskServiceImpl.deleteTask(taskId, token_id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<ResponseTaskDTO> listTasks(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity
                .ok(taskServiceImpl.listTasks(page, pageSize));
    }

}
