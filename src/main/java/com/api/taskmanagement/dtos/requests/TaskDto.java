package com.api.taskmanagement.dtos.requests;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotBlank
  private Date deadline;

  @NotBlank
  private Integer duration;

  @NotBlank
  private Boolean finished;

  @NotBlank
  private Long department_id;

  @NotBlank
  private Long person_id;
}
