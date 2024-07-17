package com.vargens.api_todo_list.service.Impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.vargens.api_todo_list.controller.dto.User.CreateUserDTO;
import com.vargens.api_todo_list.controller.dto.User.UpdateUserDTO;
import com.vargens.api_todo_list.entity.Role;
import com.vargens.api_todo_list.entity.User;
import com.vargens.api_todo_list.repository.RoleRepository;
import com.vargens.api_todo_list.repository.UserRepository;
import com.vargens.api_todo_list.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void newUser(CreateUserDTO createUserDTO) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDB = userRepository.findByUsername(createUserDTO.username());
        if (userFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(createUserDTO.username());
        user.setEmail(createUserDTO.email());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
        user.setRole(Set.of(basicRole));

        userRepository.save(user);

    }

    @Override
    public List<User> listUsers() {
        var users = userRepository.findAll();
        return users;
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDTO, UUID userId) {
        User userFromDB = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        updateUserDTO.username().ifPresent(userFromDB::setUsername);
        updateUserDTO.email().ifPresent(userFromDB::setEmail);
        updateUserDTO.password().ifPresent(userFromDB::setPassword);

    }

}
