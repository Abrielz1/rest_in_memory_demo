package com.example.rest.rest.repository.impl;

import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.ClientRepository;
import com.example.rest.rest.repository.OrderRepository;
import com.example.rest.rest.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryOrderRepository implements OrderRepository {

    private ClientRepository clientRepository;

    private final Map<Long, Order> repository = new ConcurrentHashMap<>();

    private final AtomicLong currentId = new AtomicLong(1L);


    @Override
    public List<Order> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Order> getById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Order save(Order order) {

        Long orderId = currentId.getAndIncrement();
        Long clientId = order.getClient().getId();

        Client client = clientRepository.getById(clientId).orElseThrow(
                () -> new ObjectNotFoundException(MessageFormat.format("Client with id {0} is not where", clientId))
        );


        order.setId(orderId);
        order.setClient(client);
        Instant now = Instant.now();
        order.setCreateAt(now);
        order.setUpdateAt(now);

        repository.put(orderId, order);

        client.addOrder(order);
        clientRepository.update(client);

        return order;
    }

    @Override
    public Order update(Order order) {

        Long orderId = order.getId();
        ;
        Instant now = Instant.now();

        if (!repository.containsKey(orderId)) {
            throw new ObjectNotFoundException(MessageFormat.format("Order with id {0} is not where", orderId));
        }
        Order currentOrder = repository.get(orderId);

        BeanUtils.copyNonNullProperties(order, currentOrder);

        currentOrder.setUpdateAt(now);
        currentOrder.setId(orderId);
        repository.put(orderId, currentOrder);

        return currentOrder;
    }

    @Override
    public void deleteById(Long id) {

        if (!repository.containsKey(id)) {
            throw new ObjectNotFoundException(MessageFormat.format("Order with id {0} is not where", id));
        }

        repository.remove(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        repository.forEach(repository::remove);
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
