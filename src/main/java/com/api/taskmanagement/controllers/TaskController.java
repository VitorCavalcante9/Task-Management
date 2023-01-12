package com.api.taskmanagement.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.taskmanagement.dtos.requests.AllocatePersonTaskDto;
import com.api.taskmanagement.dtos.requests.TaskDto;
import com.api.taskmanagement.models.Task;
import com.api.taskmanagement.services.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
  
  private final TaskService taskService;

  @PostMapping
  public ResponseEntity<Task> create(@RequestBody TaskDto taskDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(taskDto));
  }

  @GetMapping
  public ResponseEntity<List<Task>> list() {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
  }

  @GetMapping("/pending")
  public ResponseEntity<List<Task>> pendingTasks() {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findThreeOldestTasksWithoutPerson());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Task>> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
  }

  @PutMapping("/allocate/{id}")
  public ResponseEntity<Task> allocatePerson(@PathVariable("id") Long id, @RequestBody AllocatePersonTaskDto allocatePersonTaskDto) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.allocatePerson(id, allocatePersonTaskDto.getPerson_id()));
  }

  @PutMapping("/finish/{id}")
  public ResponseEntity<Task> finishTask(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.finishTask(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    taskService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
  }
}
