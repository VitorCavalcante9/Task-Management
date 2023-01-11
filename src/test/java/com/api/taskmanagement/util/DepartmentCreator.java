package com.api.taskmanagement.util;

import java.util.List;
import java.util.ArrayList;

import com.api.taskmanagement.models.Department;
import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.models.Task;

public class DepartmentCreator {
  
  public static Department createDepartmentToBeSaved() {
    return Department.builder().title("RH").build();
  }

  public static Department createValidDepartment() {
    return Department.builder().title("RH").id(1L).build();
  }

  public static Department createValidUpdatedDepartment() {
    return Department.builder().title("Marketing").id(1L).build();
  }

  public static Department createValidDepartmentWithPeopleAndTasks() {
    List<Person> people = new ArrayList<Person>();
    people.add(PersonCreator.createValidPerson());

    List<Task> tasks = new ArrayList<Task>();
    tasks.add(TaskCreator.createValidTask());

    return Department.builder().title("RH").id(1L).people(people).tasks(tasks).build();
  }
}
