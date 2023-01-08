package com.api.taskmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.api.taskmanagement.dtos.PersonDto;
import com.api.taskmanagement.services.PersonService;

@RestController
@RequestMapping("/people")
public class PersonController {
  
  @Autowired
  PersonService personService;

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody PersonDto personDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personDto));
  } 

  @GetMapping
  public ResponseEntity<Object> list() {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findAll());
  } 

  @GetMapping("/expenses")
  public ResponseEntity<Object> listExpenses(@RequestParam String name) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findAllWithAvgDuration(name));
  } 

  @GetMapping("/{id}")
  public ResponseEntity<Object> show(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.findById(id));
  } 

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody PersonDto personDto) {
    return ResponseEntity.status(HttpStatus.OK).body(personService.update(id, personDto));
  } 

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
    personService.delete(id);
    return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully");
  } 
}
