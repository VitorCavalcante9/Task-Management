package com.api.taskmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonDto {
  @NotBlank
  private String name;

  @NotBlank
  private Long department_id;
}
