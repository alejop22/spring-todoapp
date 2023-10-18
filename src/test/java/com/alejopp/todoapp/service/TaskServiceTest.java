package com.alejopp.todoapp.service;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;
import com.alejopp.todoapp.persistence.entity.UserEntity;
import com.alejopp.todoapp.persistence.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @MockBean // Usamos mockito para simular el taskRepository
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .email("test@test.com")
                .username("test123")
                .password("test")
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("tarea")
                .description("description task")
                .createdDate(LocalDateTime.now())
                .eta(LocalDateTime.now())
                .finished(false)
                .taskStatus(TaskStatus.ON_TIME)
                .user(user)
                .build();

        // Lo que hacemos acá es que con mockito simulamos la respuesta del repositorio cuando llama al metodo findAllByTaskStatus
        Mockito.when(taskRepository.findAllByTaskStatus(TaskStatus.ON_TIME))
                .thenReturn(Collections.singletonList(task));
    }

    @Test
    @DisplayName("Obtencion de todas las tareas con un status de tipo ON_TIME")
    public void findAllByTaskStatusOnTime() {
        List<Task> tasks = taskService.findAllByTaskStatus(TaskStatus.ON_TIME);
        for(Task task : tasks) {
            assertEquals(task.getTaskStatus(), TaskStatus.ON_TIME);
        }
    }

    @Test
    @DisplayName("Obtencion de una lista vacia cuando el status de la tarea no coincide")
    public void findAllByTaskStatusNotFound() {
        List<Task> tasks = taskService.findAllByTaskStatus(TaskStatus.LATE);
        assertEquals(tasks, Collections.EMPTY_LIST);
    }
}