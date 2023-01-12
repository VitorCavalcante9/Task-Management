package com.api.taskmanagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.taskmanagement.dtos.requests.TaskDto;
import com.api.taskmanagement.models.*;
import com.api.taskmanagement.repositories.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
  
  private final TaskRepository taskRepository;
  
  private final PersonRepository personRepository;

  private final DepartmentRepository departmentRepository;

  @Transactional
  public Task create(TaskDto taskDto) {
    Optional<Department> optionalDepartment = departmentRepository.findById(taskDto.getDepartment_id());
    if (!optionalDepartment.isPresent()) {
      throw new Error("Department not found");
    }

    Task task = new Task(taskDto.getTitle(), taskDto.getDescription(), taskDto.getDeadline(), taskDto.getDuration(), taskDto.getFinished());
    task.setDepartment(optionalDepartment.get());

    return taskRepository.save(task);
  }

  public List<Task> findAll() {
    return taskRepository.findAll();
  }

  public List<Task> findThreeOldestTasksWithoutPerson() {
    return taskRepository.findThreeOldestTasksWithoutPerson();
  }

  public Optional<Task> findById(Long id) {
    return taskRepository.findById(id);
  }

  @Transactional
  public Task allocatePerson(Long id, Long person_id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (!optionalTask.isPresent()) {
      throw new Error("Task not found");
    }

    Optional<Person> optionalPerson = personRepository.findById(person_id);
    if (!optionalPerson.isPresent()) {
      throw new Error("Person not found");
    }

    if (!optionalTask.get().getDepartment().equals(optionalPerson.get().getDepartment())) {
      throw new Error("Must be the same department");
    }

    Task task = optionalTask.get();
    task.setPerson(optionalPerson.get());

    return taskRepository.save(task);
  }

  @Transactional
  public Task finishTask(Long id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (!optionalTask.isPresent()) {
      throw new Error("Task not found");
    }

    Task task = optionalTask.get();
    task.setFinished(true);

    return taskRepository.save(task);
  }

  @Transactional
  public void delete(Long id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (!optionalTask.isPresent()) {
      throw new Error("Task not found");
    }
    taskRepository.delete(optionalTask.get());
  }
}
