package com.api.taskmanagement.repositories;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.models.Task;
import com.api.taskmanagement.util.PersonCreator;
import com.api.taskmanagement.util.TaskCreator;

@DataJpaTest
@DisplayName("Tests for Task Repository")
public class TaskRepositoryTest {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private PersonRepository personRepository;
  
  @Test
  @DisplayName("Save persists task when Successful")
  void save_PersistTask_WhenSuccessful() {
    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    Task taskSaved = this.taskRepository.save(taskToBeSaved);

    Assertions.assertThat(taskSaved).isNotNull();
    Assertions.assertThat(taskSaved.getId()).isNotNull();
    Assertions.assertThat(taskSaved.getTitle()).isEqualTo(taskToBeSaved.getTitle());
  }

  @Test
  @DisplayName("Save updates task when Successful")
  void save_UpdatesTask_WhenSuccessful() {
    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    Task taskSaved = this.taskRepository.save(taskToBeSaved);
    
    taskSaved.setTitle("Task 2");
    Task taskUpdated = this.taskRepository.save(taskSaved);

    Assertions.assertThat(taskUpdated).isNotNull();
    Assertions.assertThat(taskUpdated.getId()).isNotNull();
    Assertions.assertThat(taskUpdated.getTitle()).isEqualTo(taskSaved.getTitle());
  }

  @Test
  @DisplayName("Delete removes task when Successful")
  void delete_RemovesTask_WhenSuccessful() {
    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    Task taskSaved = this.taskRepository.save(taskToBeSaved);

    this.taskRepository.delete(taskSaved);

    Optional<Task> taskOptional = this.taskRepository.findById(taskSaved.getId());

    Assertions.assertThat(taskOptional).isEmpty();
  }

  @Test
  @DisplayName("Find by Id returns a task when Successful")
  void findById_ReturnsATask_WhenSuccessful() {
    Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
    Task taskSaved = this.taskRepository.save(taskToBeSaved);

    Long id = taskSaved.getId();

    Optional<Task> task = this.taskRepository.findById(id);

    Assertions.assertThat(task).isNotNull().isPresent();
    Assertions.assertThat(task.get().getId()).isNotNull().isEqualTo(taskSaved.getId());
  }

  @Test
  @DisplayName("Find By Id returns nothing when no task is found")
  void findById_ReturnsNothing_WhenTaskIsNotFound() {
    Optional<Task> task = this.taskRepository.findById(1L);

    Assertions.assertThat(task).isEmpty();
  }

  @Test
  @DisplayName("Find Three Oldest Tasks Without Person returns a list of tasks when Successful")
  void findThreeOldestTasksWithoutTask_ReturnsListOfTask_WhenSuccessful() {
    Task taskToBeSavedWithoutPerson = TaskCreator.createTaskToBeSaved();
    this.taskRepository.save(taskToBeSavedWithoutPerson);

    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);
    Task taskToBeSavedWithPerson = TaskCreator.createTaskToBeSaved();
    taskToBeSavedWithPerson.setPerson(personSaved);
    this.taskRepository.save(taskToBeSavedWithPerson);

    List<Task> tasks = this.taskRepository.findThreeOldestTasksWithoutPerson();

    Assertions.assertThat(tasks).isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("Find Three Oldest Tasks Without Person returns a empty list of tasks when Successful")
  void findThreeOldestTasksWithoutTask_ReturnsEmptyListOfTask_WhenNotFound() {
    List<Task> tasks = this.taskRepository.findThreeOldestTasksWithoutPerson();

    Assertions.assertThat(tasks).isEmpty();
  }

  @Test
  @DisplayName("Find All returns a list of tasks when Successful")
  void findAll_ReturnsListOfTask_WhenSuccessful() {
    Task taskToBeSavedWithoutPerson = TaskCreator.createTaskToBeSaved();
    this.taskRepository.save(taskToBeSavedWithoutPerson);

    Person personToBeSaved = PersonCreator.createPersonToBeSaved();
    Person personSaved = this.personRepository.save(personToBeSaved);
    Task taskToBeSavedWithPerson = TaskCreator.createTaskToBeSaved();
    taskToBeSavedWithPerson.setPerson(personSaved);
    this.taskRepository.save(taskToBeSavedWithPerson);

    List<Task> tasks = this.taskRepository.findAll();

    Assertions.assertThat(tasks).isNotEmpty().hasSize(2);
  }

  @Test
  @DisplayName("Find All returns a list of tasks when Successful")
  void findAll_ReturnsListOfTask_WhenNotFound() {
    List<Task> tasks = this.taskRepository.findAll();

    Assertions.assertThat(tasks).isEmpty();
  }
}
