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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryClientRepository implements ClientRepository {

    private OrderRepository orderRepository;

    private final Map<Long, Client> repository = new ConcurrentHashMap<>();


    private final AtomicLong currentId = new AtomicLong(1L);

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Client> getById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Client save(Client client) {

        Long id = currentId.getAndIncrement();
        client.setId(id);
        repository.put(id, client);

        return client;
    }

    @Override
    public Client update(Client client) {

        Long id = client.getId();

        if (!repository.containsKey(id)) {
            throw new ObjectNotFoundException(MessageFormat.format("Такого клиента с Id {0} тут нет!", id));
        }

        Client updateClient = repository.get(id);

        BeanUtils.copyNonNullProperties(client, updateClient);

        updateClient.setId(id);

        repository.replace(id, updateClient);

        return updateClient;
    }

    @Override
    public void deleteById(Long id) {

        if (!repository.containsKey(id)) {
            throw new ObjectNotFoundException(MessageFormat.format("Такого клиента с Id {0} тут нет!", id));
        }

        Client client = repository.get(id);

        orderRepository.deleteByIdIn(client.getOrders()
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList()));

        repository.remove(id);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
