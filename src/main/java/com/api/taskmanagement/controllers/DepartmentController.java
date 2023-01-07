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

import com.api.taskmanagement.dtos.DepartmentDto;
import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.services.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

  @Autowired
  DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody Department department) {
    return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.create(department));
  }

  @GetMapping
  public ResponseEntity<Object> list() {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid DepartmentDto departmentDto) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.update(id, departmentDto.getTitle()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    departmentService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Department deleted successfully");
  }
}
