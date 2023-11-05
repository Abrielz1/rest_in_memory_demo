package com.example.rest.rest.mapper.v1;

import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.OrderService;
import com.example.rest.rest.web.dto.ClientResponse;
import com.example.rest.rest.web.dto.ClientListResponse;
import com.example.rest.rest.web.dto.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    public Client createClient(Long id, Order order) {

        Client client = new Client(id,
                "Client " + id,
                new ArrayList<>()
        );

        if (order != null) {
            order.setClient(client);
            client.addOrder(order);
        }

        return client;
    }

    public Client requestToClient(UpsertClientRequest request) {

        Client client = new Client();
        client.setName(request.getName());

        return client;
    }

    public Client requestToClient(Long clientId, UpsertClientRequest request) {

        Client client = requestToClient(request);
        client.setId(clientId);

        return client;
    }

    public ClientResponse clientToResponse(Client client) {

        ClientResponse clientResponse = new ClientResponse();

        clientResponse.setId(client.getId());
        clientResponse.setName(client.getName());
        clientResponse.setOrders(orderMapper.orderListToResponseList(client.getOrders()));

        return clientResponse;
    }

    public ClientListResponse clientListToClientResponseList(List<Client> clients) {

        ClientListResponse clientListResponse = new ClientListResponse();
        clientListResponse.setClients(clients.stream().map(this::clientToResponse).collect(Collectors.toList()));

        return clientListResponse;
    }
}
