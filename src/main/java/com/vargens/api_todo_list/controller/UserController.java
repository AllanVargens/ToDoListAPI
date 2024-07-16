package com.vargens.api_todo_list.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vargens.api_todo_list.controller.dto.User.CreateUserDTO;
import com.vargens.api_todo_list.controller.dto.User.UpdateUserDTO;
import com.vargens.api_todo_list.entity.User;
import com.vargens.api_todo_list.service.Impl.UserServiceImpl;

import jakarta.transaction.Transactional;

@RestController
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDTO createUserDTO) {
        userService.newUser(createUserDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PutMapping("/users/{id}")
    @Transactional
    public ResponseEntity<Void> updateUser(@PathVariable("id") UUID userId, UpdateUserDTO updateUserDTO,
            JwtAuthenticationToken token) {
        if (!userId.equals(UUID.fromString(token.getName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.updateUser(updateUserDTO, userId);
        return ResponseEntity.ok().build();
    }
}
