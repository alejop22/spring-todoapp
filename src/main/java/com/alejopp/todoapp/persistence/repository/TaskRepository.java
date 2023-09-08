package com.alejopp.todoapp.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alejopp.todoapp.persistence.entity.Task;
import com.alejopp.todoapp.persistence.entity.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long>{

    public List<Task> findAllByTaskStatus(TaskStatus status);

    // Consulta nativa a la base de datos
    @Modifying // Indica que esto es una query de actualizacion
    @Query(value = "UPDATE tasks SET finished=true WHERE id=:id", nativeQuery = true)
    public void markTaskAsFinished(@Param("id") Long id);
}
