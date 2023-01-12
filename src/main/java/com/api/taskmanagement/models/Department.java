package com.api.taskmanagement.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
  scope = Department.class,
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
@Builder
public class Department {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String title;

  @OneToMany(mappedBy = "department")
  private List<Person> people;
  
  @OneToMany(mappedBy = "department")
  private List<Task> tasks;
}
