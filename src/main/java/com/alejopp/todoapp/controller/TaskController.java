package com.alejopp.todoapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;
import com.alejopp.todoapp.service.TaskService;
import com.alejopp.todoapp.service.dto.TaskInDTO;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    

    private final TaskService taskService;
    private final SessionRegistry sessionRegistry;

    public TaskController(TaskService taskService, SessionRegistry sessionRegistry) {
        this.taskService = taskService;
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping
    public Task createTask(@RequestBody TaskInDTO taskInDTO) {
        return this.taskService.createTask(taskInDTO);
    }

    @GetMapping
    public List<Task> findAll() {
        return this.taskService.findAll();
    }

    @GetMapping("/status/{status}")
    public List<Task> findAllByStatus(@PathVariable TaskStatus status) {
        return this.taskService.findAllByTaskStatus(status);
    }

    @PatchMapping("/mark-as-finished/{id}")
    public ResponseEntity<Void> markAsFinished(@PathVariable("id") Long id) {
        this.taskService.updateTaskFinished(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.taskService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> getDetailsSession() {

        String sessionId = "";
        User userObject = null;

        List<Object> sessions = this.sessionRegistry.getAllPrincipals(); // devulve la data de las sesiones de los usuarios

        for(Object session : sessions) {
            if (session instanceof User) {
                userObject = (User) session;
            }

            List<SessionInformation> sessionInformations = this.sessionRegistry.getAllSessions(session, false);

            for(SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> rs = new HashMap<>();

        rs.put("httpCode", 200);
        rs.put("sessionId", sessionId);
        rs.put("sessionUser", userObject);

        return ResponseEntity.ok(rs);
    }
}
