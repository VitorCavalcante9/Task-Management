package com.api.taskmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
  @Query("SELECT new com.api.taskmanagement.dtos.responses.PersonExpensesDto(p.id, p.name, avg(t.duration)) FROM Person p JOIN p.tasks t WHERE p.name = :name GROUP BY p.id")
  public List<PersonExpensesDto> findWithDurationAverage(@Param("name") String name);
  
  @Query("SELECT new com.api.taskmanagement.dtos.responses.ListPeopleDto(p.id, p.name, d.title, sum(t.duration)) FROM Person p JOIN p.tasks t JOIN p.department d GROUP BY p.id, d.title")
  public List<ListPeopleDto> findAllWithDepartmentAndTaskDuration();
}
