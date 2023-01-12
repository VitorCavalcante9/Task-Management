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

import com.api.taskmanagement.dtos.requests.AllocatePersonTaskDto;
import com.api.taskmanagement.dtos.requests.TaskDto;
import com.api.taskmanagement.models.Task;
import com.api.taskmanagement.services.TaskService;
import com.api.taskmanagement.util.PersonCreator;
import com.api.taskmanagement.util.TaskCreator;

@ExtendWith(SpringExtension.class)
public class TaskControllerTest {
  @InjectMocks
  private TaskController taskController;

  @Mock
  private TaskService taskServiceMock;

  @BeforeEach
  void setUp() {
    BDDMockito.when(taskServiceMock.findAll())
      .thenReturn(List.of(TaskCreator.createValidTask()));

    BDDMockito.when(taskServiceMock.findThreeOldestTasksWithoutPerson())
      .thenReturn(List.of(TaskCreator.createValidTask()));

    BDDMockito.when(taskServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.of(TaskCreator.createValidTask()));
    
    BDDMockito.when(taskServiceMock.allocatePerson(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
      .thenReturn(TaskCreator.createValidTaskWithPerson());
      
    BDDMockito.when(taskServiceMock.finishTask(ArgumentMatchers.anyLong()))
      .thenReturn(TaskCreator.createValidUpdatedTask());

    BDDMockito.when(taskServiceMock.create(ArgumentMatchers.any(TaskDto.class)))
      .thenReturn(TaskCreator.createValidTask());
    
    BDDMockito.doNothing().when(taskServiceMock).delete(ArgumentMatchers.anyLong());
  }

  @Test
  @DisplayName("list returns list of tasks when successful") 
  void list_ReturnsListOfTasks_WhenSuccessful() {
    Task task = TaskCreator.createValidTask();

    List<Task> tasks = taskController.list().getBody();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(tasks.get(0).getTitle()).isEqualTo(task.getTitle());
    Assertions.assertThat(tasks.get(0).getDescription()).isEqualTo(task.getDescription());
    Assertions.assertThat(tasks.get(0).getDeadline()).isEqualTo(task.getDeadline());
    Assertions.assertThat(tasks.get(0).getDuration()).isEqualTo(task.getDuration());
    Assertions.assertThat(tasks.get(0).getFinished()).isEqualTo(task.getFinished());
  }

  @Test
  @DisplayName("list returns a empty list of tasks when is not found") 
  void list_ReturnsEmptyListOfTasks_WhenIsNotFound() {
    BDDMockito.when(taskServiceMock.findAll())
      .thenReturn(List.of());

    List<Task> tasks = taskController.list().getBody();

    Assertions.assertThat(tasks).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("pendingTasks returns list of tasks when successful") 
  void pendingTasks_ReturnsListOfTasks_WhenSuccessful() {
    List<Task> tasks = taskController.pendingTasks().getBody();

    Assertions.assertThat(tasks).isNotNull().isNotEmpty().hasSize(1);
  }

  @Test
  @DisplayName("pendingTasks returns a empty list of tasks when not found") 
  void pendingTasks_ReturnsEmptyListOfTasks_WhenNotFound() {
    BDDMockito.when(taskServiceMock.findThreeOldestTasksWithoutPerson())
      .thenReturn(List.of());

    List<Task> tasks = taskController.pendingTasks().getBody(); 

    Assertions.assertThat(tasks).isNotNull().isEmpty();;
  }

  @Test
  @DisplayName("show returns task when successful") 
  void show_ReturnsTask_WhenSuccessful() {
    Long expectedId = TaskCreator.createValidTask().getId();

    Optional<Task> task = taskController.show(1L).getBody();

    Assertions.assertThat(task).isNotNull().isPresent();
    Assertions.assertThat(task.get().getId()).isEqualTo(expectedId);
  }

  @Test
  @DisplayName("show returns task when is not found") 
  void show_ReturnsTask_WhenNotFound() {
    BDDMockito.when(taskServiceMock.findById(ArgumentMatchers.anyLong()))
      .thenReturn(Optional.empty());

    Optional<Task> task = taskController.show(1L).getBody();

    Assertions.assertThat(task).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("create returns task when successful")
  void create_ReturnsTask_WhenSuccessful() {
    Task task = taskController.create(new TaskDto()).getBody();

    Assertions.assertThat(task).isNotNull().isEqualTo(TaskCreator.createValidTask());
  }

  @Test
  @DisplayName("allocatePerson returns task when successful")
  void allocatePerson_ReturnsTask_WhenSuccessful() {
    Task task = taskController.create(new TaskDto()).getBody();

    Task updatedTask = taskController.allocatePerson(1L, new AllocatePersonTaskDto(1L)).getBody();

    Assertions.assertThat(updatedTask.getId()).isNotNull().isEqualTo(task.getId());
    Assertions.assertThat(updatedTask.getPerson()).isNotNull().isEqualTo(PersonCreator.createValidPerson());
  }

  @Test
  @DisplayName("finishTask returns task when successful")
  void finishTask_ReturnsTask_WhenSuccessful() {
    Task task = taskController.create(new TaskDto()).getBody();

    Task updatedTask = taskController.finishTask(1L).getBody();

    Assertions.assertThat(updatedTask).isNotNull().isEqualTo(TaskCreator.createValidUpdatedTask());
    Assertions.assertThat(updatedTask.getId()).isNotNull().isEqualTo(task.getId());
    Assertions.assertThat(updatedTask.getFinished()).isNotNull().isEqualTo(true);
  }

  @Test
  @DisplayName("delete removes task when successful")
  void delete_RemovesTask_WhenSuccessful() {
    Assertions.assertThatCode(() -> taskController.delete(1L)).doesNotThrowAnyException();
    
    ResponseEntity<String> entity = taskController.delete(1L);
    
    Assertions.assertThat(entity).isNotNull();
    Assertions.assertThat(entity.getBody()).isNotNull().isEqualTo("Task deleted successfully");
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
