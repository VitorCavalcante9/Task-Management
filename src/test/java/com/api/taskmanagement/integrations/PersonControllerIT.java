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

import com.api.taskmanagement.dtos.requests.PersonDto;
import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.repositories.*;
import com.api.taskmanagement.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonControllerIT {
  @Autowired
  private TestRestTemplate testRestTemplate;
  @LocalServerPort
  private int port;
  
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private DepartmentRepository departmentRepository;
  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("list returns list of people with department and sum of task durations when successful") 
  void list_ReturnsListOfPeopleWithDepartmentsAndSumTaskDurations_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());

    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());
    savedPerson.setDepartment(savedDepartment);
    personRepository.save(savedPerson);

    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
    savedTask.setDepartment(savedDepartment);
    savedTask.setPerson(savedPerson);
    taskRepository.save(savedTask);
    
    String expectedName = savedPerson.getName();

    List<ListPeopleDto> listPeople = testRestTemplate.exchange("/people", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<ListPeopleDto>>() {}).getBody();

    Assertions.assertThat(listPeople).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(listPeople.get(0).getName()).isEqualTo(expectedName);
    Assertions.assertThat(listPeople.get(0).getDepartment()).isEqualTo(savedDepartment.getTitle());
    Assertions.assertThat(listPeople.get(0).getSum_duration()).isEqualTo(Long.valueOf(savedTask.getDuration()));
  }

  @Test
  @DisplayName("list returns a empty list of people when is not found") 
  void list_ReturnsEmptyListOfDepartments_WhenIsNotFound() {
    List<ListPeopleDto> listPeople = testRestTemplate.exchange("/people", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<ListPeopleDto>>() {}).getBody();

    Assertions.assertThat(listPeople).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("listExpenses returns list of people with average task durations when successful") 
  void listExpenses_ReturnsListOfPeopleWithAverageTaskDurations_WhenSuccessful() {
    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());
    personRepository.save(savedPerson);

    Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
    savedTask.setPerson(savedPerson);
    taskRepository.save(savedTask);

    String url = String.format("/people/expenses?name=%s", savedPerson.getName());

    List<PersonExpensesDto> personExpenses = testRestTemplate.exchange(url, HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<PersonExpensesDto>>() {}).getBody();

    Assertions.assertThat(personExpenses).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(personExpenses.get(0).getId()).isEqualTo(savedPerson.getId());
    Assertions.assertThat(personExpenses.get(0).getName()).isEqualTo(savedPerson.getName());
    Assertions.assertThat(personExpenses.get(0).getAvg_duration()).isEqualTo(Double.valueOf(savedTask.getDuration()));
  }

  @Test
  @DisplayName("listExpenses returns a empty list of people when is not found") 
  void listExpenses_ReturnsEmptyListOfPeople_WhenIsNotFound() {
    List<PersonExpensesDto> personExpenses = testRestTemplate.exchange("/people/expenses?name=Alan", HttpMethod.GET, null, 
      new ParameterizedTypeReference<List<PersonExpensesDto>>() {}).getBody();

    Assertions.assertThat(personExpenses).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("show returns person when successful") 
  void show_ReturnsPerson_WhenSuccessful() {
    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());
    Long expectedId = savedPerson.getId();

    Optional<Person> person = testRestTemplate.exchange("/people/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Person>>() {}, expectedId).getBody();

    Assertions.assertThat(person).isNotNull().isPresent();
    Assertions.assertThat(person.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns nothing when is not found") 
  void show_ReturnsNothing_WhenNotFound() {
    Optional<Person> person = testRestTemplate.exchange("/people/{id}", HttpMethod.GET, null, 
      new ParameterizedTypeReference<Optional<Person>>() {}, 1L).getBody();

    Assertions.assertThat(person).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns person when successful")
  void create_ReturnsPerson_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    PersonDto requestBody = new PersonDto("Alan", savedDepartment.getId());

    ResponseEntity<Person> personResponseEntity = testRestTemplate.postForEntity("/people", requestBody, Person.class);

    Assertions.assertThat(personResponseEntity).isNotNull();
    Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(personResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(personResponseEntity.getBody().getId()).isNotNull();
  }

  @Test
  @DisplayName("update returns person when successful")
  void update_ReturnsPerson_WhenSuccessful() {
    Department savedDepartment = departmentRepository.save(DepartmentCreator.createDepartmentToBeSaved());
    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());

    PersonDto requestBody = new PersonDto("Rafael", savedDepartment.getId());

    String url = String.format("/people/%s", savedPerson.getId());
    
    ResponseEntity<Person> personResponseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(requestBody), Person.class);

    Assertions.assertThat(personResponseEntity);
    Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(personResponseEntity.getBody()).isNotNull();
    Assertions.assertThat(personResponseEntity.getBody().getId()).isNotNull().isEqualTo(savedPerson.getId());
    Assertions.assertThat(personResponseEntity.getBody().getName()).isNotNull().isEqualTo(requestBody.getName());
  }

  @Test
  @DisplayName("delete removes person when successful")
  void delete_RemovesPerson_WhenSuccessful() {
    Person savedPerson = personRepository.save(PersonCreator.createPersonToBeSaved());

    ResponseEntity<String> personResponseEntity = testRestTemplate.exchange("/people/{id}", HttpMethod.DELETE, null, String.class, savedPerson.getId());

    Assertions.assertThat(personResponseEntity).isNotNull();
    Assertions.assertThat(personResponseEntity.getBody()).isNotNull().isEqualTo("Person deleted successfully");
    Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

}
