package com.api.taskmanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.taskmanagement.dtos.requests.PersonDto;
import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.repositories.DepartmentRepository;
import com.api.taskmanagement.repositories.PersonRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {
  
  private final PersonRepository personRepository;

  private final DepartmentRepository departmentRepository;

  @Transactional
  public Person create(PersonDto personDto) {
    Optional<Department> optionalDepartment = departmentRepository.findById(personDto.getDepartment_id());
    if (!optionalDepartment.isPresent()) {
      throw new Error("Department not found");
    }
    Department department = optionalDepartment.get();

    Person person = new Person();
    person.setName(personDto.getName());
    person.setDepartment(department);

    return personRepository.save(person);
  }

  public List<ListPeopleDto> findAll() {
    return personRepository.findAllWithDepartmentAndTaskDuration();
  }

  public List<PersonExpensesDto> findAllWithAvgDuration(String name) {
    return personRepository.findWithDurationAverage(name);
  }

  public Optional<Person> findById(Long id) {
    return personRepository.findById(id);
  }

  @Transactional
  public Person update(Long id, PersonDto personDto) {
    Optional<Person> optionalPerson = personRepository.findById(id);
    if (!optionalPerson.isPresent()) {
      throw new Error("Person not found");
    }

    Optional<Department> optionalDepartment = departmentRepository.findById(personDto.getDepartment_id());
    if (!optionalDepartment.isPresent()) {
      throw new Error("Department not found");
    }
    Department department = optionalDepartment.get();
    
    Person person = optionalPerson.get();
    person.setName(personDto.getName());
    person.setDepartment(department);

    return personRepository.save(person);
  }

  @Transactional
  public void delete(Long id) {
    Optional<Person> optionalPerson = personRepository.findById(id);
    if (!optionalPerson.isPresent()) {
      throw new Error("Person not found");
    }
    personRepository.delete(optionalPerson.get());
  }
}
