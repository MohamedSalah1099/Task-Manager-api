package com.mohamedsalah.taskmanager.repository;

import com.mohamedsalah.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access and dynamic query specification executor for Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByUserId(Long userId, Pageable pageable);

    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
