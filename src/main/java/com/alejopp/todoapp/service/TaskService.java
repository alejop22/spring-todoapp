package com.alejopp.todoapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alejopp.todoapp.mapper.TaskInDTOToTask;
import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;
import com.alejopp.todoapp.persistence.repository.TaskRepository;
import com.alejopp.todoapp.service.dto.TaskInDTO;

@Service
public class TaskService {
    
    private final TaskRepository repository;
    private final TaskInDTOToTask mapper;

    public TaskService(TaskRepository repository, TaskInDTOToTask mapper) { // Inyeccion de dependencias sin el Autowired, es mejor practica
        this.repository = repository;
        this.mapper = mapper;
    }

    public Task createTask(TaskInDTO taskInDTO) {

        Task task = mapper.map(taskInDTO);

        return this.repository.save(task);
    }

    public List<Task> findAll() {
        return this.repository.findAll();
    }

    public List<Task> findAllByTaskStatus(TaskStatus status) {
        return this.repository.findAllByTaskStatus(status);
    }

} 
