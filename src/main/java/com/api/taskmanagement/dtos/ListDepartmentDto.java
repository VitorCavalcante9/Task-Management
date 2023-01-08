package com.api.taskmanagement.dtos;

import lombok.Data;

@Data
public class ListDepartmentDto {
  private Long id;

  private String title;

  private Long count_people;

  private Long count_tasks;

  public ListDepartmentDto(Long id, String title, Long count_people, Long count_tasks) {
    this.id = id;
    this.title = title;
    this.count_people = count_people;
    this.count_tasks = count_tasks;
  }
}
