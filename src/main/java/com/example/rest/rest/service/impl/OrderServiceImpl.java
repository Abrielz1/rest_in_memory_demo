package com.example.rest.rest.service.impl;

import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.exceptions.UpdateStateException;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.OrderRepository;
import com.example.rest.rest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.getById(id)
                .orElseThrow(() -> new  ObjectNotFoundException(
                        MessageFormat.format("Order with id {0} is not where", id)));
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        checkForUpdate(order.getId());
        return orderRepository.update(order);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        orderRepository.deleteByIdIn(ids);
    }

    private void checkForUpdate(Long orderId) {
        Order currentOrder = findById(orderId);
        Instant now = Instant.now();
        Duration duration = Duration.between(currentOrder.getUpdateAt(), now);
        if (duration.getSeconds() > 5) {
            throw new UpdateStateException(MessageFormat.format("Time is UP for order with id {0}", orderId));
        }
    }
}
