package com.api.taskmanagement.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDto {
  @NotBlank
  private String title;
}
