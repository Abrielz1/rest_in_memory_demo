package com.example.rest.rest;

import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.web.dto.ClientResponse;
import com.example.rest.rest.web.dto.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class AbstractTestController {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper mapper;

    public Client createClient(Long id, Order order) {

        Client client = new Client();

        client.setId(id);
        client.setName("Client " + id);
        client.setOrders(new ArrayList<>());

        if (order != null) {
            order.setClient(client);
            client.addOrder(order);
        }

        return client;
    }

    public Order createOrder(Long id, Long cost, Client client) {

        Order order = new Order();

        order.setId(id);
        order.setProduct("Test product " + id);
        order.setCost(BigDecimal.valueOf(cost));
        order.setClient(client);
        order.setCreateAt(Instant.now());
        order.setUpdateAt(Instant.now());

        return order;
    }

    public ClientResponse createClientResponse(Long id, OrderResponse response) {

        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setId(id);
        clientResponse.setName("Client " + id);
        clientResponse.setOrders(new ArrayList<>());

        if (response != null) {
            clientResponse.getOrders().add(response);
        }

        return clientResponse;
    }

    public OrderResponse createOrderResponse(Long id, Long cost) {

        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(id);
        orderResponse.setCost(BigDecimal.valueOf(cost));
        orderResponse.setProduct("Test product " + id);

        return orderResponse;
    }
}
