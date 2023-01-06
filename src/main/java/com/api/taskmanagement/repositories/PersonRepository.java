package com.api.taskmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.taskmanagement.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
  
}
