package com.api.taskmanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.util.*;

@DataJpaTest
@DisplayName("Tests for Department Repository")
public class DepartmentRepositoryTest {
  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("Save persists department when Successful")
  void save_PersistDepartment_WhenSuccessful() {
    Department departmentToBeSaved = DepartmentCreator.createDepartmentToBeSaved();
    Department departmentSaved = this.departmentRepository.save(departmentToBeSaved);

    Assertions.assertThat(departmentSaved).isNotNull();
    Assertions.assertThat(departmentSaved.getId()).isNotNull();
    Assertions.assertThat(departmentSaved.getTitle()).isEqualTo(departmentToBeSaved.getTitle());
  }

  @Test
  @DisplayName("Save updates department when Successful")
  void save_UpdatesDepartment_WhenSuccessful() {
    Department departmentToBeSaved = DepartmentCreator.createDepartmentToBeSaved();
    Department departmentSaved = this.departmentRepository.save(departmentToBeSaved);
    
    departmentSaved.setTitle("Comercial");
    Department departmentUpdated = this.departmentRepository.save(departmentSaved);

    Assertions.assertThat(departmentUpdated).isNotNull();
    Assertions.assertThat(departmentUpdated.getId()).isNotNull();
    Assertions.assertThat(departmentUpdated.getTitle()).isEqualTo(departmentSaved.getTitle());
  }

  @Test
  @DisplayName("Delete removes department when Successful")
  void delete_RemovesPerson_WhenSuccessful() {
    Department departmentToBeSaved = DepartmentCreator.createDepartmentToBeSaved();
    Department departmentSaved = this.departmentRepository.save(departmentToBeSaved);

    this.departmentRepository.delete(departmentSaved);

    Optional<Department> departmentOptional = this.departmentRepository.findById(departmentSaved.getId());

    Assertions.assertThat(departmentOptional).isEmpty();
  }

  @Test
  @DisplayName("Find by Title returns a department when Successful")
  void findByTitle_ReturnsADepartment_WhenSuccessful() {
    Department departmentToBeSaved = DepartmentCreator.createDepartmentToBeSaved();
    Department departmentSaved = this.departmentRepository.save(departmentToBeSaved);

    String title = departmentSaved.getTitle();

    Optional<Department> department = this.departmentRepository.findByTitle(title);

    Assertions.assertThat(department).isNotNull().isPresent();
    Assertions.assertThat(department.get().getId()).isNotNull().isEqualTo(departmentSaved.getId());
  }

  @Test
  @DisplayName("Find By Title returns nothing when no department is found")
  void findByTitle_ReturnsNothing_WhenDepartmentIsNotFound() {
    Optional<Department> department = this.departmentRepository.findByTitle("Marketing");

    Assertions.assertThat(department).isEmpty();
  }

  @Test
  @DisplayName("Find All With People and Tasks returns a list of ListDepartmentDto when Successful")
  void findAllWithPeopleAndTasks_returnsListDepartmentDto_WhenSuccessful() {
    Department departmentToBeSaved = DepartmentCreator.createDepartmentToBeSaved();
    Department departmentSaved = this.departmentRepository.save(departmentToBeSaved);

    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    personToBeSaved.setDepartment(departmentSaved);
    this.personRepository.save(personToBeSaved);

    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    taskToBeSaved.setDepartment(departmentSaved);
    this.taskRepository.save(taskToBeSaved);

    List<ListDepartmentDto> departments = this.departmentRepository.findAllWithPeopleAndTasks();

    Assertions.assertThat(departments).isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("Find All With People and Tasks returns a list of ListDepartmentDto when not found")
  void findAllWithPeopleAndTasks_returnsListDepartmentDto_WhenNotFound() {
    List<ListDepartmentDto> departments = this.departmentRepository.findAllWithPeopleAndTasks();

    Assertions.assertThat(departments).isEmpty();
  }
}
