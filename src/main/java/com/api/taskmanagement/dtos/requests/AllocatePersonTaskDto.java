package com.api.taskmanagement.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AllocatePersonTaskDto {
  @NotBlank
  private Long person_id;
}
