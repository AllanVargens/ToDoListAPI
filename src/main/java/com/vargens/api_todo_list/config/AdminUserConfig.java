package com.vargens.api_todo_list.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vargens.api_todo_list.entity.Role;
import com.vargens.api_todo_list.entity.User;
import com.vargens.api_todo_list.repository.RoleRepository;
import com.vargens.api_todo_list.repository.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin already exist");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setEmail("admin@gmail.com");
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setRole(Set.of(roleAdmin));
                    userRepository.save(user);
                });
    }

}
