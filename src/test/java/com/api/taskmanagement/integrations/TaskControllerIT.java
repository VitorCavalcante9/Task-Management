package com.api.taskmanagement.integrations;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.api.taskmanagement.dtos.requests.AllocatePersonTaskDto;
import com.api.taskmanagement.dtos.requests.TaskDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.repositories.*;
import com.api.taskmanagement.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskControllerIT {
  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;
  
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private DepartmentRepository departmentRepository;

  @Test
  @DisplayName("list returns list of tasks when successful") 
  void list_ReturnsListOfTasks_WhenSuccessful() {
    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());

    List<Task> tasks = testRestTemplate.exchange("/tasks", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Task>>() {}).getBody();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(savedTask.getTitle());
    Assertions.assertThat(tasks.get(0).getDescription()).isEqualTo(savedTask.getDescription());
    Assertions.assertThat(tasks.get(0).getDuration()).isEqualTo(savedTask.getDuration());
    Assertions.assertThat(tasks.get(0).getFinished()).isEqualTo(savedTask.getFinished());
  }

  @Test
  @DisplayName("list returns a empty list of tasks when is not found") 
  void list_ReturnsEmptyListOfTasks_WhenIsNotFound() {
    List<Task> tasks = testRestTemplate.exchange("/tasks", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Task>>() {}).getBody();

    Assertions.assertThat(tasks).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("pendingTasks returns list of tasks when successful") 
  void pendingTasks_ReturnsListOfTasks_WhenSuccessful() {
    taskRepository.save(TaskCreator.createTaskToBeSaved());

    List<Task> tasks = testRestTemplate.exchange("/tasks/pending", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Task>>() {}).getBody();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("pendingTasks returns a empty list of tasks when not found") 
  void pendingTasks_ReturnsEmptyListOfTasks_WhenNotFound() {
    List<Task> tasks = testRestTemplate.exchange("/tasks/pending", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<Task>>() {}).getBody(); 

    Assertions.assertThat(tasks).isNotNull().isEmpty();;
  }

  @Test
  @DisplayName("show returns task when successful") 
  void show_ReturnsTask_WhenSuccessful() {
    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
    Long expectedId = savedTask.getId();

    Optional<Task> task = testRestTemplate.exchange("/tasks/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Task>>() {}, expectedId).getBody(); 

    Assertions.assertThat(task).isNotNull().isPresent();
    Assertions.assertThat(task.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns nothing when is not found") 
  void show_ReturnsNothing_WhenNotFound() {
    Optional<Task> task = testRestTemplate.exchange("/tasks/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Task>>() {}, 1L).getBody(); 

    Assertions.assertThat(task).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns task when successful")
  void create_ReturnsTask_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    TaskDto requestBody = new TaskDto("Task A", "A Task", Date.valueOf("2022-12-30"), 2, false, savedDepartment.getId(), null);

    ResponseEntity<Task> taskResponseEntity = testRestTemplate.postForEntity("/tasks", requestBody, Task.class);

    Assertions.assertThat(taskResponseEntity).isNotNull();
    Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull();
  }

  @Test
  @DisplayName("allocatePerson returns task when successful")
  void allocatePerson_ReturnsTask_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());

    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());
    savedPerson.setDepartment(savedDepartment);
    personRepository.save(savedPerson);

    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
    savedTask.setDepartment(savedDepartment);
    taskRepository.save(savedTask);

    String url = String.format("/tasks/allocate/%s", savedTask.getId());

    AllocatePersonTaskDto requestBody = new AllocatePersonTaskDto(savedPerson.getId());

    ResponseEntity<Task> taskResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestBody), Task.class);

    Assertions.assertThat(taskResponseEntity);
    Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull().isEqualTo(savedTask.getId());
    Assertions.assertThat(taskResponseEntity.getBody().getPerson()).isNotNull();
    Assertions.assertThat(taskResponseEntity.getBody().getPerson().getId()).isNotNull().isEqualTo(requestBody.getPerson_id());
  }

  @Test
  @DisplayName("finishTask returns task when successful")
  void finishTask_ReturnsTask_WhenSuccessful() {
    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());

    String url = String.format("/tasks/finish/%s", savedTask.getId());

    ResponseEntity<Task> taskResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, null, Task.class);

    Assertions.assertThat(taskResponseEntity);
    Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull().isEqualTo(savedTask.getId());
    Assertions.assertThat(taskResponseEntity.getBody().getFinished()).isNotNull().isEqualTo(true);
  }

  @Test
  @DisplayName("delete removes task when successful")
  void delete_RemovesTask_WhenSuccessful() {
    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());

    ResponseEntity<String> taskResponseEntity = testRestTemplate.exchange("/tasks/{id}", HttpMethod.DELETE, null, String.class, savedTask.getId());

    Assertions.assertThat(taskResponseEntity).isNotNull();
    Assertions.assertThat(taskResponseEntity.getBody()).isNotNull().isEqualTo("Task deleted successfully");
    Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
