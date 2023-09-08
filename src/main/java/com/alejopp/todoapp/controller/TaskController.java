package com.alejopp.todoapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.service.TaskService;
import com.alejopp.todoapp.service.dto.TaskInDTO;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody TaskInDTO taskInDTO) {
        return this.taskService.createTask(taskInDTO);
    }

}
