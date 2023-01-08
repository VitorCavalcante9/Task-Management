package com.api.taskmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.taskmanagement.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
  @Query("SELECT t FROM Task t WHERE t.person IS NULL ORDER BY t.deadline ASC LIMIT 3")
  public List<Task> findThreeOldestTasksWithoutPerson();
}
