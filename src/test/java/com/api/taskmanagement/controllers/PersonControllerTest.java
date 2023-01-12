package com.api.taskmanagement.controllers;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.taskmanagement.dtos.requests.PersonDto;
import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.services.PersonService;
import com.api.taskmanagement.util.PersonCreator;

@ExtendWith(SpringExtension.class)
public class PersonControllerTest {
  @InjectMocks
  private PersonController personController;

  @Mock
  private PersonService personServiceMock;

  @BeforeEach
  void setUp() {
    PersonExpensesDto personExpenses = new PersonExpensesDto(1L, "Alan", 2.0);
    ListPeopleDto listPeople = new ListPeopleDto(1L, "Alan", "Desenvolvimento", 10L);

    BDDMockito.when(personServiceMock.findAllWithAvgDuration(ArgumentMatchers.anyString()))
      .thenReturn(List.of(personExpenses));

    BDDMockito.when(personServiceMock.findAll())
      .thenReturn(List.of(listPeople));

    BDDMockito.when(personServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(PersonCreator.createValidPerson()));

    BDDMockito.when(personServiceMock.create(ArgumentMatchers.any(PersonDto.class)))
      .thenReturn(PersonCreator.createValidPerson());

    BDDMockito.when(personServiceMock.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PersonDto.class)))
      .thenReturn(PersonCreator.createValidUpdatedPerson());
    
    BDDMockito.doNothing().when(personServiceMock).delete(ArgumentMatchers.anyLong());
  }

  @Test
  @DisplayName("list returns list of people with department and sum of task durations when successful") 
  void list_ReturnsListOfPeopleWithDepartmentsAndSumTaskDurations_WhenSuccessful() {
    String expectedName = PersonCreator.createValidPerson().getName();

    List<ListPeopleDto> listPeople = personController.list().getBody();

    Assertions.assertThat(listPeople).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(listPeople.get(0).getName()).isEqualTo(expectedName);
    Assertions.assertThat(listPeople.get(0).getDepartment()).isEqualTo("Desenvolvimento");
    Assertions.assertThat(listPeople.get(0).getSum_duration()).isEqualTo(10L);
  }

  @Test
  @DisplayName("list returns a empty list of people when is not found") 
  void list_ReturnsEmptyListOfDepartments_WhenIsNotFound() {
    BDDMockito.when(personServiceMock.findAll())
      .thenReturn(List.of());

    List<ListPeopleDto> listPeople = personController.list().getBody();

    Assertions.assertThat(listPeople).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("listExpenses returns list of people with average task durations when successful") 
  void listExpenses_ReturnsListOfPeopleWithAverageTaskDurations_WhenSuccessful() {
    Person personSaved = PersonCreator.createValidPerson();
    List<PersonExpensesDto> personExpenses = personController.listExpenses(personSaved.getName()).getBody();

    Assertions.assertThat(personExpenses).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(personExpenses.get(0).getId()).isEqualTo(personSaved.getId());
    Assertions.assertThat(personExpenses.get(0).getName()).isEqualTo(personSaved.getName());
    Assertions.assertThat(personExpenses.get(0).getAvg_duration()).isEqualTo(2.0);
  }

  @Test
  @DisplayName("listExpenses returns a empty list of people when is not found") 
  void listExpenses_ReturnsEmptyListOfPeople_WhenIsNotFound() {
    BDDMockito.when(personServiceMock.findAllWithAvgDuration(ArgumentMatchers.anyString()))
      .thenReturn(List.of());

    List<PersonExpensesDto> personExpenses = personController.listExpenses("Alan").getBody();

    Assertions.assertThat(personExpenses).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("show returns person when successful") 
  void show_ReturnsPerson_WhenSuccessful() {
    Long expectedId = PersonCreator.createValidPerson().getId();

    Optional<Person> person = personController.show(1L).getBody();

    Assertions.assertThat(person).isNotNull().isPresent();
    Assertions.assertThat(person.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns nothing when is not found") 
  void show_ReturnsNothing_WhenNotFound() {
    BDDMockito.when(personServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Person> person = personController.show(1L).getBody();

    Assertions.assertThat(person).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns person when successful")
  void create_ReturnsPerson_WhenSuccessful() {
    Person person = personController.create(new PersonDto("Alan", 1L)).getBody();

    Assertions.assertThat(person).isNotNull().isEqualTo(PersonCreator.createValidPerson());
  }

  @Test
  @DisplayName("update returns person when successful")
  void update_ReturnsPerson_WhenSuccessful() {
    Person person = personController.create(new PersonDto("Alan", 1L)).getBody();

    Person updatedPerson = personController.update(1L, new PersonDto("Rafael", 1L)).getBody();

    Assertions.assertThat(updatedPerson).isNotNull().isEqualTo(PersonCreator.createValidUpdatedPerson());
    Assertions.assertThat(updatedPerson.getId()).isNotNull().isEqualTo(person.getId());
    Assertions.assertThat(updatedPerson.getName()).isNotNull().isNotEqualTo(person.getName());
  }

  @Test
  @DisplayName("delete removes person when successful")
  void delete_RemovesPerson_WhenSuccessful() {
    Assertions.assertThatCode(() -> personController.delete(1L)).doesNotThrowAnyException();

    ResponseEntity<String> entity = personController.delete(1L);

    Assertions.assertThat(entity).isNotNull();
    Assertions.assertThat(entity.getBody()).isNotNull().isEqualTo("Person deleted successfully");
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
  
}
