package com.mohamedsalah.taskmanager.repository;

import com.mohamedsalah.taskmanager.entity.Role;
import com.mohamedsalah.taskmanager.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access operations for Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
