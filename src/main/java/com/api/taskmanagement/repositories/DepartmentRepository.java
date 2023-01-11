package com.api.taskmanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
  Optional<Department> findByTitle(String title);

  @Query("SELECT new com.api.taskmanagement.dtos.responses.ListDepartmentDto(d.id, d.title, count(distinct p.id), count(distinct t.id)) FROM Department d JOIN d.tasks t JOIN d.people p GROUP BY d.id")
  public List<ListDepartmentDto> findAllWithPeopleAndTasks();
}
