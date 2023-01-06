package com.api.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.taskmanagement.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
  
}
