package com.example.payroll.controller.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
