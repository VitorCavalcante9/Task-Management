package com.api.taskmanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.repositories.DepartmentRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService {

  @Autowired
  DepartmentRepository departmentRepository;

  @Transactional
  public Department create(Department department) {
    Optional<Department> existsDepartment = departmentRepository.findByTitle(department.getTitle());

    if (existsDepartment.isPresent()) {
      throw new Error("Department already exists");
    }

    return departmentRepository.save(department);
  }

  public List<Department> findAll() {
    return departmentRepository.findAll();
  }

  public Optional<Department> findById(Long id) {
    return departmentRepository.findById(id);
  }

  @Transactional
  public Department update(Long id, String title) {
    Optional<Department> optionalDepartment = departmentRepository.findById(id);
    if (!optionalDepartment.isPresent()) {
      throw new Error("Department not found");
    }
    Department department = optionalDepartment.get();
    department.setTitle(title);
    return departmentRepository.save(department);
  }

  @Transactional
  public void delete(Long id) {
    Optional<Department> department = departmentRepository.findById(id);
    if (!department.isPresent()) {
      throw new Error("Department not found");
    }
    departmentRepository.delete(department.get());
  }
}
