package com.alejopp.todoapp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alejopp.todoapp.persistence.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
