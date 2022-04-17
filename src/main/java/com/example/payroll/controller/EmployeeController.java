package com.example.payroll.controller;

import com.example.payroll.controller.exceptions.EmployeeNotFoundException;
import com.example.payroll.entity.Employee;
import com.example.payroll.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class EmployeeController {
    private final EmployeeRepository repository;


    EmployeeController(EmployeeRepository employeeRepository) {
        this.repository = employeeRepository;
    }
    /*
    Aggregate root
    tag::get-aggregate-root[]
     */
    @GetMapping("/employees")
    List<Employee> all(){
        return repository.findAll();
    }
    /*
    end::get-aggregate-root[]
     */
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee){
        return repository.save(newEmployee);
    }
    /*
    Single item
     */
    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }
    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id ){
        repository.deleteById(id);
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
