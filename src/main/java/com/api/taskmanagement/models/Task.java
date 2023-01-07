package com.api.taskmanagement.models;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
public class Task implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
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
}
