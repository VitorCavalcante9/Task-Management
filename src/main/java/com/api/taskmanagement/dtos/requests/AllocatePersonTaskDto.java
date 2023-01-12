package com.api.taskmanagement.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocatePersonTaskDto {
  @NotBlank
  private Long person_id;
}
