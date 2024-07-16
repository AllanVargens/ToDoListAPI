package com.vargens.api_todo_list.controller;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vargens.api_todo_list.controller.dto.Task.CreateTaskDto;
import com.vargens.api_todo_list.controller.dto.Task.ReponseTaskDTO;
import com.vargens.api_todo_list.controller.dto.Task.TaskItemDTO;
import com.vargens.api_todo_list.entity.Role;
import com.vargens.api_todo_list.entity.Task;
import com.vargens.api_todo_list.repository.TaskRepository;
import com.vargens.api_todo_list.repository.UserRepository;

@RestController
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDto taskDto,
            JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var task = new Task();
        task.setUser(user.get());
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());

        taskRepository.save(task);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRole()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || task.getUser().getId().equals(UUID.fromString(token.getName()))) {
            taskRepository.deleteById(taskId);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<ReponseTaskDTO> listTasks(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        var tasks = taskRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "dueDate"))
                .map(task -> new TaskItemDTO(task.getId(), task.getTitle(), task.getDescription()));

        return ResponseEntity
                .ok(new ReponseTaskDTO(tasks.getContent(), page, pageSize, tasks.getTotalPages(),
                        tasks.getTotalElements()));
    }

}
