package com.example.rest.rest.web.controller;

import com.example.rest.rest.mapper.v1.OrderMapper;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.dto.OrderListResponse;
import com.example.rest.rest.web.dto.OrderResponse;
import com.example.rest.rest.web.dto.UpsertOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    private final OrderMapper mapper;

    @GetMapping
    public ResponseEntity<OrderListResponse> findAll() {
        return ResponseEntity.ok(mapper.orderListToOrderResponse(service.findAll()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(mapper.orderToResponse(service.findById(orderId)));
    }


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody UpsertOrderRequest request) {

        Order newOrder = service.save(mapper.requestToOrder(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.orderToResponse(newOrder));
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("id") Long orderID,
                                                     @RequestBody UpsertOrderRequest request) {

        Order updateOrder = service.update(mapper.requestToOrder(orderID, request));
        return ResponseEntity.ok(mapper.orderToResponse(updateOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long orderId) {

        service.deleteById(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
