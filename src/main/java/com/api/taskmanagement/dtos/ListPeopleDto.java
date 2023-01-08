package com.api.taskmanagement.dtos;

import lombok.Data;

@Data
public class ListPeopleDto {
  private Long id;

  private String name;

  private String department;

  private Long sum_duration;

  public ListPeopleDto(Long id, String name, String department, Long sum_duration) {
    this.id = id;
    this.name = name;
    this.department = department;
    this.sum_duration = sum_duration;
  }
}
