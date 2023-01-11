package com.api.taskmanagement.util;

import com.api.taskmanagement.models.Department;

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
}
