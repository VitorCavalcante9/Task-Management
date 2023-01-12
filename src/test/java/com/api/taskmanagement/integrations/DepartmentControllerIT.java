package com.api.taskmanagement.integrations;

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

import com.api.taskmanagement.dtos.requests.DepartmentDto;
import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.repositories.*;
import com.api.taskmanagement.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DepartmentControllerIT {
  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;

  @Autowired
  private DepartmentRepository departmentRepository;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private TaskRepository taskRepository;
  
  @Test
  @DisplayName("list returns list of department with count people and count task when successful") 
  void list_ReturnsListOfDepartmentsWithCountPeopleAndTask_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    String expectedTitle = savedDepartment.getTitle();

    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());
    savedPerson.setDepartment(savedDepartment);
    personRepository.save(savedPerson);

    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
    savedTask.setDepartment(savedDepartment);
    taskRepository.save(savedTask);

    List<ListDepartmentDto> departments = testRestTemplate.exchange("/departments", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<ListDepartmentDto>>() {}).getBody();

    Assertions.assertThat(departments).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(departments.get(0).getTitle()).isEqualTo(expectedTitle);
  }
  
  @Test
  @DisplayName("list returns a empty list of department when is not found")  
  void list_ReturnsListOfDepartmentsWithCountPeopleANdTask_WhenNotFound() {
    List<ListDepartmentDto> departments = testRestTemplate.exchange("/departments", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<ListDepartmentDto>>() {}).getBody();

    Assertions.assertThat(departments).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("show returns department when successful") 
  void show_ReturnsDepartment_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    Long expectedId = savedDepartment.getId();

    Optional<Department> department = testRestTemplate.exchange("/departments/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Department>>() {}, expectedId).getBody();

    Assertions.assertThat(department).isNotNull().isPresent();
    Assertions.assertThat(department.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns nothing when not found") 
  void show_ReturnsNothing_WhenNotFound() {
    Optional<Department> department = testRestTemplate.exchange("/departments/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Department>>() {}, 1L).getBody();
      
    Assertions.assertThat(department).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns department when successful")
  void create_ReturnsDepartment_WhenSuccessful() {
    ResponseEntity<Department> departmentResponseEntity = testRestTemplate.postForEntity("/departments", DepartmentCreator.createDepartmentToBeSaved(), Department.class);

    Assertions.assertThat(departmentResponseEntity).isNotNull();
    Assertions.assertThat(departmentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(departmentResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(departmentResponseEntity.getBody().getId()).isNotNull();
  }

  @Test
  @DisplayName("update returns department when successful")
  void update_ReturnsDepartment_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    DepartmentDto requestBody = new DepartmentDto("Marketing");

    String url = String.format("/departments/%s", savedDepartment.getId());

    ResponseEntity<Department> departmentResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestBody), Department.class);
    
    Assertions.assertThat(departmentResponseEntity).isNotNull();
    Assertions.assertThat(departmentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(departmentResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(departmentResponseEntity.getBody().getId()).isNotNull().isEqualTo(savedDepartment.getId());
    Assertions.assertThat(departmentResponseEntity.getBody().getTitle()).isNotNull().isEqualTo(requestBody.getTitle());
  }

  @Test
  @DisplayName("delete removes department when successful")
  void delete_RemovesDepartment_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());

    ResponseEntity<String> departmentResponseEntity = testRestTemplate.exchange("/departments/{id}", HttpMethod.DELETE, null, String.class, savedDepartment.getId());

    Assertions.assertThat(departmentResponseEntity).isNotNull();
    Assertions.assertThat(departmentResponseEntity.getBody()).isNotNull().isEqualTo("Department deleted successfully");
    Assertions.assertThat(departmentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
