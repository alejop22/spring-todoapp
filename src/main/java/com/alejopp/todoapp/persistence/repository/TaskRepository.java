package com.alejopp.todoapp.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long>{

    public List<Task> findAllByTaskStatus(TaskStatus status);

}
