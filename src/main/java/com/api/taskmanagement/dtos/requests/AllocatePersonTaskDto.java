package com.api.taskmanagement.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AllocatePersonTaskDto {
  @NotBlank
  private Long person_id;
}
