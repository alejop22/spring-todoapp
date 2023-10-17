package com.alejopp.todoapp.persistence.repository;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;
import com.alejopp.todoapp.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Siginifica que haremos test a la persistencia
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach // Esto se ejecuta antes que todos los tests
    void setUp() {
        UserEntity user =
                UserEntity.builder()
                        .email("test@test.com")
                        .username("test")
                        .password("test")
                        .tasks(Collections.emptySet())
                        .build();
        testEntityManager.persist(user);

        Task task =
                Task.builder()
                        .title("Test")
                        .description("Test task description")
                        .createdDate(LocalDateTime.now())
                        .eta(LocalDateTime.now())
                        .finished(false)
                        .taskStatus(TaskStatus.ON_TIME)
                        .user(user)
                        .build();

        testEntityManager.persist(task);
    }

    @Test
    public void findAllByTaskStatusOnTime() {
        List<Task> tasks = taskRepository.findAllByTaskStatus(TaskStatus.ON_TIME);
        for(Task task : tasks) {
            assertEquals(task.getTaskStatus(), TaskStatus.ON_TIME);
        }
    }
    @Test
    public void findAllByTaskStatusNotFound() {
        List<Task> tasks = taskRepository.findAllByTaskStatus(TaskStatus.LATE);
        assertEquals(tasks, Collections.EMPTY_LIST);
    }
}