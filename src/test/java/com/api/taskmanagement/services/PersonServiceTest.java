package com.api.taskmanagement.services;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.taskmanagement.dtos.requests.PersonDto;
import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.repositories.DepartmentRepository;
import com.api.taskmanagement.repositories.PersonRepository;
import com.api.taskmanagement.util.DepartmentCreator;
import com.api.taskmanagement.util.PersonCreator;

@ExtendWith(SpringExtension.class)
public class PersonServiceTest {
  @InjectMocks
  private PersonService personService;

  @Mock
  private PersonRepository personRepositoryMock;

  @Mock
  private DepartmentRepository departmentRepositoryMock;

  @BeforeEach
  void setUp() {
    PersonExpensesDto personExpenses = new PersonExpensesDto(1L, "Alan", 2.0);
    ListPeopleDto listPeople = new ListPeopleDto(1L, "Alan", "Desenvolvimento", 10L);

    BDDMockito.when(departmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(DepartmentCreator.createValidDepartment()));

    BDDMockito.when(personRepositoryMock.findWithDurationAverage(ArgumentMatchers.anyString()))
      .thenReturn(List.of(personExpenses));

    BDDMockito.when(personRepositoryMock.findAllWithDepartmentAndTaskDuration())
      .thenReturn(List.of(listPeople));

    BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(PersonCreator.createValidPerson()));

    BDDMockito.when(personRepositoryMock.save(ArgumentMatchers.any(Person.class)))
      .thenReturn(PersonCreator.createValidPerson());
    
    BDDMockito.doNothing().when(personRepositoryMock).delete(ArgumentMatchers.any(Person.class));
  }

  @Test
  @DisplayName("listAll returns list of people with department and sum of task durations when successful") 
  void listAll_ReturnsListOfPeopleWithDepartmentsAndSumTaskDurations_WhenSuccessful() {
    String expectedName = PersonCreator.createValidPerson().getName();

    List<ListPeopleDto> listPeople = personService.findAll();

    Assertions.assertThat(listPeople).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(listPeople.get(0).getName()).isEqualTo(expectedName);
    Assertions.assertThat(listPeople.get(0).getDepartment()).isEqualTo("Desenvolvimento");
    Assertions.assertThat(listPeople.get(0).getSum_duration()).isEqualTo(10L);
  }

  @Test
  @DisplayName("listAll returns a empty list of people when is not found") 
  void listAll_ReturnsEmptyListOfDepartments_WhenIsNotFound() {
    BDDMockito.when(personRepositoryMock.findAllWithDepartmentAndTaskDuration())
      .thenReturn(List.of());

      List<ListPeopleDto> listPeople = personService.findAll();

    Assertions.assertThat(listPeople).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findAllWithAvgDuration returns list of people with average task durations when successful") 
  void findAllWithAvgDuration_ReturnsListOfPeopleWithAverageTaskDurations_WhenSuccessful() {
    Person personSaved = PersonCreator.createValidPerson();
    List<PersonExpensesDto> personExpenses = personService.findAllWithAvgDuration(personSaved.getName());

    Assertions.assertThat(personExpenses).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(personExpenses.get(0).getId()).isEqualTo(personSaved.getId());
    Assertions.assertThat(personExpenses.get(0).getName()).isEqualTo(personSaved.getName());
    Assertions.assertThat(personExpenses.get(0).getAvg_duration()).isEqualTo(2.0);
  }

  @Test
  @DisplayName("findAllWithAvgDuration returns a empty list of people when is not found") 
  void findAllWithAvgDuration_ReturnsEmptyListOfPeople_WhenIsNotFound() {
    BDDMockito.when(personRepositoryMock.findWithDurationAverage(ArgumentMatchers.anyString()))
      .thenReturn(List.of());

    List<PersonExpensesDto> personExpenses = personService.findAllWithAvgDuration("Alan");

    Assertions.assertThat(personExpenses).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns person when successful") 
  void findById_ReturnsPerson_WhenSuccessful() {
    Long expectedId = PersonCreator.createValidPerson().getId();

    Optional<Person> person = personService.findById(1L);

    Assertions.assertThat(person).isNotNull().isPresent();
    Assertions.assertThat(person.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("findById returns person when is not found") 
  void findById_ReturnsPerson_WhenNotFound() {
    BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Person> person = personService.findById(1L);

    Assertions.assertThat(person).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns person when successful")
  void create_ReturnsPerson_WhenSuccessful() {
    Person person = personService.create(new PersonDto("Alan", 1L));

    Assertions.assertThat(person).isNotNull().isEqualTo(PersonCreator.createValidPerson());
  }

  @Test
  @DisplayName("update returns person when successful")
  void update_ReturnsPerson_WhenSuccessful() {
    Person person = personService.create(new PersonDto("Alan", 1L));
    
    BDDMockito.when(personRepositoryMock.save(ArgumentMatchers.any(Person.class)))
      .thenReturn(PersonCreator.createValidUpdatedPerson());

    Person updatedPerson = personService.update(1L, new PersonDto("Rafael", 1L));

    Assertions.assertThat(updatedPerson).isNotNull().isEqualTo(PersonCreator.createValidUpdatedPerson());
    Assertions.assertThat(updatedPerson.getId()).isNotNull().isEqualTo(person.getId());
    Assertions.assertThat(updatedPerson.getName()).isNotNull().isNotEqualTo(person.getName());
  }

  @Test
  @DisplayName("delete removes person when successful")
  void delete_RemovesPerson_WhenSuccessful() {
    Assertions.assertThatCode(() -> personService.delete(1L)).doesNotThrowAnyException();
  }
  
}
