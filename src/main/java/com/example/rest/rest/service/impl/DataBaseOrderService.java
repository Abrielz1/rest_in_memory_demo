package com.example.rest.rest.service.impl;

import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.repository.impl.DataBaseOrderRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataBaseOrderService implements OrderService {

    private final DataBaseOrderRepository dataBaseOrderRepository;

    private final ClientService dataBaseClientService;

    @Override
    public List<Order> findAll() {
        return dataBaseOrderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return dataBaseOrderRepository.findById(id)
                .orElseThrow( () -> new ObjectNotFoundException(
                        MessageFormat.format("Order with id = {0} not found!", id)
                ));
    }

    @Override
    public Order save(Order order) {
        return dataBaseOrderRepository.save(order);
    }

    @Override
    public Order update(Order order) {

        checkForUpdate(order.getId());

        Client client = dataBaseClientService.getById(order.getClient().getId());
        Order dbOrder = dataBaseOrderRepository.findById(order.getId()).orElseThrow( () -> new ObjectNotFoundException(
                MessageFormat.format("Order with id = {0} not found!", order.getId())
        ));

        BeanUtils.copyNonNullProperties(order, dbOrder);

        dbOrder.setClient(client); //?

        return dataBaseOrderRepository.save(dbOrder);
    }

    @Override
    public void deleteById(Long id) {
        dataBaseOrderRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        dataBaseOrderRepository.deleteAllById(ids);
    }
}
