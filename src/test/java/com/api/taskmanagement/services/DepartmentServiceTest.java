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

import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.repositories.DepartmentRepository;
import com.api.taskmanagement.util.DepartmentCreator;

@ExtendWith(SpringExtension.class)
public class DepartmentServiceTest {
  @InjectMocks
  private DepartmentService departmentService;

  @Mock
  private DepartmentRepository departmentRepositoryMock;

  @BeforeEach
  void setUp() {
    ListDepartmentDto listDepartment = new ListDepartmentDto(1L, "RH", 1L, 1L);
    BDDMockito.when(departmentRepositoryMock.findAllWithPeopleAndTasks())
      .thenReturn(List.of(listDepartment));

    BDDMockito.when(departmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(DepartmentCreator.createValidDepartment()));
    
    BDDMockito.when(departmentRepositoryMock.findByTitle(ArgumentMatchers.anyString()))
      .thenReturn(Optional.of(DepartmentCreator.createValidDepartment()));

    BDDMockito.when(departmentRepositoryMock.save(ArgumentMatchers.any(Department.class)))
      .thenReturn(DepartmentCreator.createValidDepartment());
    
    BDDMockito.doNothing().when(departmentRepositoryMock).delete(ArgumentMatchers.any(Department.class));
  }

  @Test
  @DisplayName("listAll returns list of department with count people and count task when successful") 
  void listAll_ReturnsListOfDepartmentsWithCountPeopleAndTask_WhenSuccessful() {
    String expectedTitle = DepartmentCreator.createValidDepartment().getTitle();

    List<ListDepartmentDto> departments = departmentService.findAll();

    Assertions.assertThat(departments).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(departments.get(0).getTitle()).isEqualTo(expectedTitle);
    Assertions.assertThat(departments.get(0).getCount_people()).isEqualTo(1L);
    Assertions.assertThat(departments.get(0).getCount_tasks()).isEqualTo(1L);
  }

  @Test
  @DisplayName("listAll returns a empty list of department when is not found") 
  void listAll_ReturnsEmptyListOfDepartments_WhenISNotFound() {
    BDDMockito.when(departmentRepositoryMock.findAllWithPeopleAndTasks())
      .thenReturn(List.of());

    List<ListDepartmentDto> departments = departmentService.findAll();

    Assertions.assertThat(departments).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findById returns department when successful") 
  void findById_ReturnsDepartment_WhenSuccessful() {
    Long expectedId = DepartmentCreator.createValidDepartment().getId();

    Optional<Department> department = departmentService.findById(1L);

    Assertions.assertThat(department).isNotNull().isPresent();
    Assertions.assertThat(department.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("findById returns department when is not found") 
  void findById_ReturnsDepartment_WhenNotFound() {
    BDDMockito.when(departmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Department> department = departmentService.findById(1L);

    Assertions.assertThat(department).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns department when successful")
  void create_ReturnsDepartment_WhenSuccessful() {
    Department department = departmentService.create(new Department());

    Assertions.assertThat(department).isNotNull().isEqualTo(DepartmentCreator.createValidDepartment());
  }

  @Test
  @DisplayName("update returns department when successful")
  void update_ReturnsDepartment_WhenSuccessful() {
    Department department = departmentService.create(new Department());
    
    BDDMockito.when(departmentRepositoryMock.save(ArgumentMatchers.any(Department.class)))
      .thenReturn(DepartmentCreator.createValidUpdatedDepartment());

    Department updatedDepartment = departmentService.update(1L, "Marketing");

    Assertions.assertThat(updatedDepartment).isNotNull().isEqualTo(DepartmentCreator.createValidUpdatedDepartment());
    Assertions.assertThat(updatedDepartment.getId()).isNotNull().isEqualTo(department.getId());
    Assertions.assertThat(updatedDepartment.getTitle()).isNotNull().isNotEqualTo(department.getTitle());
  }

  @Test
  @DisplayName("delete removes department when successful")
  void delete_RemovesDepartment_WhenSuccessful() {
    Assertions.assertThatCode(() -> departmentService.delete(1L)).doesNotThrowAnyException();
  }
}
