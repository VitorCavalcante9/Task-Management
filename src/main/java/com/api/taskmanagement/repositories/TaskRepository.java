package com.api.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.taskmanagement.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
  
}
