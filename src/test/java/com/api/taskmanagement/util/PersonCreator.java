package com.api.taskmanagement.util;

import com.api.taskmanagement.models.Person;

public class PersonCreator {
  
  public static Person createPersonToBeSaved() {
    return Person.builder().name("Alan").build();
  }

  public static Person createValidPerson() {
    return Person.builder().name("Alan").id(1L).build();
  }

  public static Person createValidPersonWithDepartment() {
    return Person.builder().name("Alan").id(1L).department(DepartmentCreator.createValidDepartment()).build();
  }

  public static Person createValidUpdatedPerson() {
    return Person.builder().name("Rafael").id(1L).build();
  }
}
