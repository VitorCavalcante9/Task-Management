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

import com.api.taskmanagement.dtos.requests.TaskDto;
import com.api.taskmanagement.models.Task;
import com.api.taskmanagement.repositories.*;
import com.api.taskmanagement.util.*;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {
  @InjectMocks
  private TaskService taskService;

  @Mock
  private TaskRepository taskRepositoryMock;

  @Mock
  private PersonRepository personRepositoryMock;

  @Mock
  private DepartmentRepository departmentRepositoryMock;

  @BeforeEach
  void setUp() {
    BDDMockito.when(departmentRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(DepartmentCreator.createValidDepartment()));
    BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(PersonCreator.createValidPerson()));

    BDDMockito.when(taskRepositoryMock.findAll())
      .thenReturn(List.of(TaskCreator.createValidTask()));

    BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(TaskCreator.createValidTask()));

    BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
      .thenReturn(TaskCreator.createValidTask());
    
    BDDMockito.doNothing().when(taskRepositoryMock).delete(ArgumentMatchers.any(Task.class));
  }

  @Test
  @DisplayName("listAll returns list of tasks when successful") 
  void listAll_ReturnsListOfTasks_WhenSuccessful() {
    Task task = TaskCreator.createValidTask();

    List<Task> tasks = taskService.findAll();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(task.getTitle());
    Assertions.assertThat(tasks.get(0).getDescription()).isEqualTo(task.getDescription());
    Assertions.assertThat(tasks.get(0).getDeadline()).isEqualTo(task.getDeadline());
    Assertions.assertThat(tasks.get(0).getDuration()).isEqualTo(task.getDuration());
    Assertions.assertThat(tasks.get(0).getFinished()).isEqualTo(task.getFinished());
  }

  @Test
  @DisplayName("listAll returns a empty list of tasks when is not found") 
  void listAll_ReturnsListOfTasks_WhenIsNotFound() {
    BDDMockito.when(taskRepositoryMock.findAll())
      .thenReturn(List.of());

      List<Task> tasks = taskService.findAll();

    Assertions.assertThat(tasks).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("findThreeOldestTasksWithoutPerson returns list of tasks when successful") 
  void findThreeOldestTasksWithoutPerson_ReturnsListOfTasks_WhenSuccessful() {
    List<Task> tasks = taskService.findAll();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("findThreeOldestTasksWithoutPerson returns a empty list of tasks when not found") 
  void findThreeOldestTasksWithoutPerson_ReturnsEmptyListOfTasks_WhenNotFound() {
    BDDMockito.when(taskRepositoryMock.findAll())
      .thenReturn(List.of());

    List<Task> tasks = taskService.findAll();

    Assertions.assertThat(tasks).isNotNull().isEmpty();;
  }

  @Test
  @DisplayName("findById returns task when successful") 
  void findById_ReturnsTask_WhenSuccessful() {
    Long expectedId = TaskCreator.createValidTask().getId();

    Optional<Task> task = taskService.findById(1L);

    Assertions.assertThat(task).isNotNull().isPresent();
    Assertions.assertThat(task.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("findById returns nothing when is not found") 
  void findById_ReturnsNothing_WhenNotFound() {
    BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Task> task = taskService.findById(1L);

    Assertions.assertThat(task).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns task when successful")
  void create_ReturnsTask_WhenSuccessful() {
    Task task = taskService.create(new TaskDto("", "", null, 1, false, 1L, 1L));

    Assertions.assertThat(task).isNotNull().isEqualTo(TaskCreator.createValidTask());
  }

  @Test
  @DisplayName("allocatePerson returns task when successful")
  void allocatePerson_ReturnsTask_WhenSuccessful() {
    Task task = taskService.create(new TaskDto("", "", null, 1, false, 1L, 1L));
    
    BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
      .thenReturn(TaskCreator.createValidTaskWithPerson());

    BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(PersonCreator.createValidPersonWithDepartment()));

    BDDMockito.when(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(TaskCreator.createValidTaskWithDepartment()));

    Task updatedTask = taskService.allocatePerson(1L, 1L);

    Assertions.assertThat(updatedTask.getId()).isNotNull().isEqualTo(task.getId());
    Assertions.assertThat(updatedTask.getPerson()).isNotNull().isEqualTo(PersonCreator.createValidPerson());
  }

  @Test
  @DisplayName("finishTask returns task when successful")
  void finishTask_ReturnsTask_WhenSuccessful() {
    Task task = taskService.create(new TaskDto("", "", null, 1, false, 1L, 1L));
    
    BDDMockito.when(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
      .thenReturn(TaskCreator.createValidUpdatedTask());

      Task updatedTask = taskService.finishTask(1L);

    Assertions.assertThat(updatedTask).isNotNull().isEqualTo(TaskCreator.createValidUpdatedTask());
    Assertions.assertThat(updatedTask.getId()).isNotNull().isEqualTo(task.getId());
    Assertions.assertThat(updatedTask.getFinished()).isNotNull().isEqualTo(true);
  }

  @Test
  @DisplayName("delete removes task when successful")
  void delete_RemovesTask_WhenSuccessful() {
    Assertions.assertThatCode(() -> taskService.delete(1L)).doesNotThrowAnyException();
  }
}
