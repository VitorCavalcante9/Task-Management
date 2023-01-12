package com.api.taskmanagement.util;

import java.sql.Date;

import com.api.taskmanagement.models.Task;

public class TaskCreator {
  
  public static Task createTaskToBeSaved() {
    return Task.builder()
            .title("Task 1")
            .deadline(Date.valueOf("2022-12-30"))
            .description("A Task")
            .duration(2)
            .finished(false)
            .build();
  }

  public static Task createValidTask() {
    return Task.builder()
            .title("Task 1")
            .deadline(Date.valueOf("2022-12-30"))
            .description("A Task")
            .duration(2)
            .finished(false)
            .id(1L).build();
  }

  public static Task createValidTaskWithDepartment() {
    return Task.builder()
            .title("Task 1")
            .deadline(Date.valueOf("2022-12-30"))
            .description("A Task")
            .duration(2)
            .finished(false)
            .department(DepartmentCreator.createValidDepartment())
            .id(1L).build();
  }

  public static Task createValidTaskWithPerson() {
    return Task.builder()
            .title("Task 1")
            .deadline(Date.valueOf("2022-12-30"))
            .description("A Task")
            .duration(2)
            .finished(false)
            .person(PersonCreator.createValidPerson())
            .department(DepartmentCreator.createValidDepartment())
            .id(1L).build();
  }

  public static Task createValidUpdatedTask() {
    return Task.builder()
            .title("Task 2")
            .deadline(Date.valueOf("2022-12-30"))
            .description("A Task")
            .duration(2)
            .finished(true)
            .id(1L).build();
  }
}
