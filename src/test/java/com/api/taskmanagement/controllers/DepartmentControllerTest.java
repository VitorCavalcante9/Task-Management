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

import com.api.taskmanagement.dtos.requests.DepartmentDto;
import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.services.DepartmentService;
import com.api.taskmanagement.util.DepartmentCreator;

@ExtendWith(SpringExtension.class)
public class DepartmentControllerTest {
  @InjectMocks
  private DepartmentController departmentController;

  @Mock
  private DepartmentService departmentServiceMock;

  @BeforeEach
  void setUp() {
    ListDepartmentDto listDepartment = new ListDepartmentDto(1L, "RH", 1L, 1L);
    BDDMockito.when(departmentServiceMock.findAll())
      .thenReturn(List.of(listDepartment));

    BDDMockito.when(departmentServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(DepartmentCreator.createValidDepartment()));

    BDDMockito.when(departmentServiceMock.create(ArgumentMatchers.any(Department.class)))
      .thenReturn(DepartmentCreator.createValidDepartment());
    
    BDDMockito.when(departmentServiceMock.update(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
      .thenReturn(DepartmentCreator.createValidUpdatedDepartment());
    
    BDDMockito.doNothing().when(departmentServiceMock).delete(ArgumentMatchers.anyLong());
  }

  @Test
  @DisplayName("list returns list of department with count people and count task when successful") 
  void list_ReturnsListOfDepartmentsWithCountPeopleANdTask_WhenSuccessful() {
    String expectedTitle = DepartmentCreator.createValidDepartment().getTitle();

    List<ListDepartmentDto> departments = departmentController.list().getBody();

    Assertions.assertThat(departments).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(departments.get(0).getTitle()).isEqualTo(expectedTitle);
    Assertions.assertThat(departments.get(0).getCount_people()).isEqualTo(1L);
    Assertions.assertThat(departments.get(0).getCount_tasks()).isEqualTo(1L);
  }

  @Test
  @DisplayName("list returns a empty list of department when is not found") 
  void list_ReturnsEmptyListOfDepartments_WhenISNotFound() {
    BDDMockito.when(departmentServiceMock.findAll())
      .thenReturn(List.of());

    List<ListDepartmentDto> departments = departmentController.list().getBody();

    Assertions.assertThat(departments).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("show returns department when successful") 
  void show_ReturnsDepartment_WhenSuccessful() {
    Long expectedId = DepartmentCreator.createValidDepartment().getId();

    Optional<Department> department = departmentController.show(1L).getBody();

    Assertions.assertThat(department).isNotNull().isPresent();
    Assertions.assertThat(department.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns department when is not found") 
  void show_ReturnsDepartment_WhenNotFound() {
    BDDMockito.when(departmentServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Department> department = departmentController.show(1L).getBody();

    Assertions.assertThat(department).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns department when successful")
  void create_ReturnsDepartment_WhenSuccessful() {
    Department department = departmentController.create(new Department()).getBody();

    Assertions.assertThat(department).isNotNull().isEqualTo(DepartmentCreator.createValidDepartment());
  }

  @Test
  @DisplayName("update returns department when successful")
  void update_ReturnsDepartment_WhenSuccessful() {
    Department department = departmentController.create(new Department()).getBody();
    
    BDDMockito.when(departmentServiceMock.create(ArgumentMatchers.any(Department.class)))
      .thenReturn(DepartmentCreator.createValidUpdatedDepartment());

    Department updatedDepartment = departmentController.update(1L, new DepartmentDto("Marketing")).getBody();

    Assertions.assertThat(updatedDepartment).isNotNull().isEqualTo(DepartmentCreator.createValidUpdatedDepartment());
    Assertions.assertThat(updatedDepartment.getId()).isNotNull().isEqualTo(department.getId());
    Assertions.assertThat(updatedDepartment.getTitle()).isNotNull().isNotEqualTo(department.getTitle());
  }

  @Test
  @DisplayName("delete removes department when successful")
  void delete_RemovesDepartment_WhenSuccessful() {
    Assertions.assertThatCode(() -> departmentController.delete(1L)).doesNotThrowAnyException();

    ResponseEntity<String> entity = departmentController.delete(1L);
    
    Assertions.assertThat(entity).isNotNull();
    Assertions.assertThat(entity.getBody()).isNotNull().isEqualTo("Department deleted successfully");
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
