package com.alejopp.todoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alejopp.todoapp.context.security.jwt.JwtUtils;
import com.alejopp.todoapp.exceptions.ToDoExceptions;
import com.alejopp.todoapp.mapper.TaskInDTOToTask;
import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;
import com.alejopp.todoapp.persistence.entity.UserEntity;
import com.alejopp.todoapp.persistence.repository.TaskRepository;
import com.alejopp.todoapp.persistence.repository.UserRepository;
import com.alejopp.todoapp.service.dto.TaskInDTO;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    
    private final TaskRepository repository;
    private final UserRepository userRepository;
    private final TaskInDTOToTask mapper;
    private final JwtUtils jwtUtils;

    public TaskService(TaskRepository repository, TaskInDTOToTask mapper, JwtUtils jwtUtils, UserRepository userRepository) { // Inyeccion de dependencias sin el Autowired, es mejor practica
        this.repository = repository;
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Transactional // Esto garantiza que todas las operaciones de JPA se hagan en una misma transaccion
    public Task createTask(TaskInDTO taskInDTO, String token) {
        try {
            UserEntity user = getUserFromToken(token);
            
            Task task = mapper.map(taskInDTO);
            task.setUser(user);
    
            return this.repository.save(task);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Task> findAll() {
        return this.repository.findAll();
    }

    public List<Task> findAllByTaskStatus(TaskStatus status) {
        return this.repository.findAllByTaskStatus(status);
    }

    @Transactional // Se decora de esta manera ya que esta operacion hace un cambio de datos
    public void updateTaskFinished(Long id) {

        Optional<Task> optionalTask = this.repository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new ToDoExceptions("Task not found", HttpStatus.NOT_FOUND);
        }

        this.repository.markTaskAsFinished(id);
    }

    public void deleteById(Long id) {

        Optional<Task> optionalTask = this.repository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new ToDoExceptions("Task not found", HttpStatus.NOT_FOUND);
        }

        this.repository.deleteById(id);
    }

    private UserEntity getUserFromToken(String token) {
        String username = this.jwtUtils.getUsernameFromToken(token);

        return this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
    }

} 
