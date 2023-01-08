package com.api.taskmanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.taskmanagement.dtos.PersonExpensesDto;
import com.api.taskmanagement.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
  @Query("SELECT new com.api.taskmanagement.dtos.PersonExpensesDto(p.id, p.name, avg(t.duration)) FROM Person p JOIN p.tasks t WHERE p.name = :name GROUP BY p.id")
  public List<PersonExpensesDto> findWithDurationAverage(@Param("name") String name);
}
