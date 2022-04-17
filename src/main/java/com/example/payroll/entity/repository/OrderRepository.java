package com.example.payroll.entity.repository;

import com.example.payroll.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
