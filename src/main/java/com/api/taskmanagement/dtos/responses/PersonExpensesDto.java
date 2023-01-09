package com.api.taskmanagement.dtos.responses;

import lombok.Data;

@Data
public class PersonExpensesDto {
  private Long id;

  private String name;

  private Double avg_duration;

  public PersonExpensesDto(Long id, String name, Double avg_duration) {
    this.id = id;
    this.name = name;
    this.avg_duration = avg_duration;
  }
}
