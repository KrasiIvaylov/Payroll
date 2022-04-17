package com.example.payroll.controller.advice;

import com.example.payroll.controller.exceptions.EmployeeNotFoundException;
import com.example.payroll.controller.exceptions.OrderNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderNotFoundAdvice {
    @ResponseBody //signals that this advice is rendered straight into the response body.
    @ExceptionHandler //configures the advice to only respond if an OrderNotFoundException is thrown.
    @ResponseStatus//says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
    String OrderNotFoundHandler(OrderNotFoundException ex){
     /*
    the advice generates the content. In this case, it gives the message of the exception.
     */
        return ex.getMessage();
    }
}
