package com.alejopp.todoapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alejopp.todoapp.mapper.CreateUserDTO;
import com.alejopp.todoapp.persistence.entity.UserEntity;
import com.alejopp.todoapp.persistence.repository.UserRepository;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(CreateUserDTO createUserDTO) {
        
        UserEntity user = UserEntity.builder()
            .email(createUserDTO.getEmail())
            .username(createUserDTO.getUsername())
            .password(this.passwordEncoder.encode(createUserDTO.getPassword()))
            .build();

        this.userRepository.save(user);

        return user;
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);
        return users;
    }

    public void deleteUser(String id) {
        this.userRepository.deleteById(Long.parseLong(id));
    }
}
