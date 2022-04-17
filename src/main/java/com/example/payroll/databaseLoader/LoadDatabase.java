package com.example.payroll.databaseLoader;

import com.example.payroll.Order;
import com.example.payroll.entity.Employee;
import com.example.payroll.entity.enums.Status;
import com.example.payroll.entity.repository.EmployeeRepository;
import com.example.payroll.entity.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
            employeeRepository.save(new Employee("Luke", "Skywalker", "son"));
            employeeRepository.save(new Employee("Anakin", "Skywalker", "father"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("Green Light Saber", Status.COMPLETED));
            orderRepository.save(new Order("Red Light Saber", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
