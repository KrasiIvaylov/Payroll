package com.example.payroll.controller;

import com.example.payroll.controller.assemblers.EmployeeModelAssembler;
import com.example.payroll.controller.exceptions.EmployeeNotFoundException;
import com.example.payroll.entity.Employee;
import com.example.payroll.entity.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public
class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;


    EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler assembler) {
        this.repository = employeeRepository;
        this.assembler = assembler;
    }

    /*
    Aggregate root
    tag::get-aggregate-root[]
     */
    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

    }

    /*
    end::get-aggregate-root[]
     */
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee){
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
    Single item
     */
    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);

    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*


    @RestController indicates that the data returned by each
    method will be written straight into the response body instead of rendering a template.

    An EmployeeRepository is injected by constructor into the controller.

    We have routes for each operation (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping,
    corresponding to HTTP GET, POST, PUT, and DELETE calls).

    EmployeeNotFoundException is an exception used to indicate when an employee is looked up but not found.


     */

}
