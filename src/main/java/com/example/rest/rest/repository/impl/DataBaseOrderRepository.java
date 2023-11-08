package com.example.rest.rest.repository.impl;

import com.example.rest.rest.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataBaseOrderRepository extends JpaRepository<Order, Long> {
}
