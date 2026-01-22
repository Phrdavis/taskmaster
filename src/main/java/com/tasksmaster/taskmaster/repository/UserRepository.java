package com.tasksmaster.taskmaster.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasksmaster.taskmaster.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByDeleted(String deleted, Pageable pageable);

    Page<User> findByNameAndDeleted(String name, String deleted, Pageable pageable);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    
}
