package com.api.taskmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.api.taskmanagement.dtos.AllocatePersonTaskDto;
import com.api.taskmanagement.dtos.TaskDto;
import com.api.taskmanagement.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  
  @Autowired
  TaskService taskService;

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody TaskDto taskDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(taskDto));
  }

  @GetMapping
  public ResponseEntity<Object> list() {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
  }

  @GetMapping("/pending")
  public ResponseEntity<Object> pendingTasks() {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findThreeOldestTasksWithoutPerson());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
  }

  @PutMapping("/allocate/{id}")
  public ResponseEntity<Object> allocatePerson(@PathVariable("id") Long id, @RequestBody AllocatePersonTaskDto allocatePersonTaskDto) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.allocatePerson(id, allocatePersonTaskDto.getPerson_id()));
  }

  @PutMapping("/finish/{id}")
  public ResponseEntity<Object> finishTask(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.finishTask(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    taskService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
  }
}
