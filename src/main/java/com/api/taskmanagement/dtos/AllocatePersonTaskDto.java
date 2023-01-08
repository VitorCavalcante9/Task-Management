package com.api.taskmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AllocatePersonTaskDto {
  @NotBlank
  private Long person_id;
}
