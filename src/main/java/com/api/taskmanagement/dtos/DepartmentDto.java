package com.api.taskmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDto {
  @NotBlank
  private String title;
}
