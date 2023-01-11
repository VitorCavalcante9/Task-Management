package com.api.taskmanagement.models;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
@Builder
public class Task implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date deadline;

  @Column(nullable = false)
  private Integer duration;

  @Column(nullable = false)
  private Boolean finished;
  
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "department_id")
  private Department department;
  
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "person_id")
  private Person person;

  public Task(String title, String description, Date deadline, Integer duration, Boolean finished) {
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.duration = duration;
    this.finished = finished;
  }
}
