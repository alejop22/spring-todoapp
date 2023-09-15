package com.alejopp.todoapp.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alejopp.todoapp.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    Optional<UserEntity> findByUsername(String username); // Esta consulta la hace automaticamente JPA
}
