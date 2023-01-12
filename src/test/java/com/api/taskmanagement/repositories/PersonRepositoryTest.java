package com.api.taskmanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.util.*;

@DataJpaTest
@DisplayName("Tests for Person Repository")
public class PersonRepositoryTest {
  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("Save persists person when Successful")
  void save_PersistPerson_WhenSuccessful() {
    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);

    Assertions.assertThat(personSaved).isNotNull();
    Assertions.assertThat(personSaved.getId()).isNotNull();
    Assertions.assertThat(personSaved.getName()).isEqualTo(personToBeSaved.getName());
  }

  @Test
  @DisplayName("Save updates person when Successful")
  void save_UpdatesPerson_WhenSuccessful() {
    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);
    
    personSaved.setName("Rafael");
    Person personUpdated = this.personRepository.save(personSaved);

    Assertions.assertThat(personUpdated).isNotNull();
    Assertions.assertThat(personUpdated.getId()).isNotNull();
    Assertions.assertThat(personUpdated.getName()).isEqualTo(personSaved.getName());
  }

  @Test
  @DisplayName("Delete removes person when Successful")
  void delete_RemovesPerson_WhenSuccessful() {
    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);

    this.personRepository.delete(personSaved);

    Optional<Person> personOptional = this.personRepository.findById(personSaved.getId());

    Assertions.assertThat(personOptional).isEmpty();
  }

  @Test
  @DisplayName("Find by Id returns a person when Successful")
  void findById_ReturnsAPerson_WhenSuccessful() {
    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);

    Long id = personSaved.getId();

    Optional<Person> person = this.personRepository.findById(id);

    Assertions.assertThat(person).isNotNull().isPresent();
    Assertions.assertThat(person.get().getId()).isNotNull().isEqualTo(personSaved.getId());
  }

  @Test
  @DisplayName("Find By Id returns nothing when no person is found")
  void findById_ReturnsNothing_WhenPersonIsNotFound() {
    Optional<Person> person = this.personRepository.findById(1L);

    Assertions.assertThat(person).isEmpty();
  }

  @Test
  @DisplayName("Find With Duration Average returns a list of PersonExpensesDto when Successful")
  void findAllWithDurationAverage_returnsPersonExpensesDto_WhenSuccessful() {
    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);

    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    taskToBeSaved.setPerson(personSaved);
    this.taskRepository.save(taskToBeSaved);

    List<PersonExpensesDto> personExpenses = this.personRepository.findWithDurationAverage(personSaved.getName());

    Assertions.assertThat(personExpenses).isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("Find With Duration Average returns a empty list of PersonExpensesDto when not found")
  void findAllWithDurationAverage_returnsEmptyListPersonExpensesDto_WhenNotFound() {
    List<PersonExpensesDto> personExpenses = this.personRepository.findWithDurationAverage("Alan");

    Assertions.assertThat(personExpenses).isEmpty();
  }

  @Test
  @DisplayName("Find With Department and Task Duration returns a list of ListPeopleDto when Successful")
  void findAllWithDepartmentAndTaskDuration_returnsListPeopleDto_WhenSuccessful() {
    Department departmentSaved = this.departmentRepository.save(DepartmentCreator.createValidDepartment());

    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    personToBeSaved.setDepartment(departmentSaved);
    Person personSaved = this.personRepository.save(personToBeSaved);

    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    taskToBeSaved.setDepartment(departmentSaved);
    taskToBeSaved.setPerson(personSaved);
    this.taskRepository.save(taskToBeSaved);

    List<ListPeopleDto> listPeople = this.personRepository.findAllWithDepartmentAndTaskDuration();

    Assertions.assertThat(listPeople).isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("Find With Department and Task Duration returns a empty list of ListPeopleDto when not found")
  void findAllWithDepartmentAndTaskDuration_returnsEmptyListPeopleDto_WhenNotFound() {
    List<ListPeopleDto> listPeople = this.personRepository.findAllWithDepartmentAndTaskDuration();

    Assertions.assertThat(listPeople).isEmpty();
  }
}
