package com.vargens.api_todo_list.service;

import java.util.Optional;
import java.util.UUID;

import com.vargens.api_todo_list.controller.dto.Task.CreateTaskDto;
import com.vargens.api_todo_list.controller.dto.Task.ResponseTaskDTO;
import com.vargens.api_todo_list.entity.User;

public interface TaskService {
    void createTask(CreateTaskDto createTaskDto, Optional<User> user);

    void deleteTask(Long taskId, UUID token_id);

    ResponseTaskDTO listTasks(int page, int pageSize);
}
