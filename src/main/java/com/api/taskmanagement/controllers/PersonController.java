package com.api.taskmanagement.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.taskmanagement.dtos.requests.PersonDto;
import com.api.taskmanagement.dtos.responses.ListPeopleDto;
import com.api.taskmanagement.dtos.responses.PersonExpensesDto;
import com.api.taskmanagement.models.Person;
import com.api.taskmanagement.services.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PersonController {
  
  private final PersonService personService;

  @PostMapping
  public ResponseEntity<Person> create(@RequestBody PersonDto personDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personDto));
  } 

  @GetMapping
  public ResponseEntity<List<ListPeopleDto>> list() {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findAll());
  } 

  @GetMapping("/expenses")
  public ResponseEntity<List<PersonExpensesDto>> listExpenses(@RequestParam String name) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findAllWithAvgDuration(name));
  } 

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Person>> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
  } 

  @PutMapping("/{id}")
  public ResponseEntity<Person> update(@PathVariable("id") Long id, @RequestBody PersonDto personDto) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.update(id, personDto));
  } 

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    personService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully");
  } 
}
