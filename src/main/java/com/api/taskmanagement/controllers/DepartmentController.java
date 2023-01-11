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

import com.api.taskmanagement.dtos.requests.DepartmentDto;
import com.api.taskmanagement.dtos.responses.ListDepartmentDto;
import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.services.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

  private final DepartmentService departmentService;

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody Department department) {
    return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.create(department));
  }

  @GetMapping
  public ResponseEntity<List<ListDepartmentDto>> list() {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Department>> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> update(@PathVariable("id") Long id, @RequestBody @Valid DepartmentDto departmentDto) {
    return ResponseEntity.status(HttpStatus.OK).body(departmentService.update(id, departmentDto.getTitle()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    departmentService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Department deleted successfully");
  }
}
