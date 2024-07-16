package com.vargens.api_todo_list.service;

import java.util.List;
import java.util.UUID;

import com.vargens.api_todo_list.controller.dto.User.CreateUserDTO;
import com.vargens.api_todo_list.controller.dto.User.UpdateUserDTO;
import com.vargens.api_todo_list.entity.User;

public interface UserService {
    void newUser(CreateUserDTO createUserDTO);

    List<User> listUsers();

    void updateUser(UpdateUserDTO updateUserDTO, UUID userId);
}
