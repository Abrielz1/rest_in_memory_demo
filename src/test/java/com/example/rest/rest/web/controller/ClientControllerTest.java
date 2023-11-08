package com.example.rest.rest.web.controller;

import com.example.rest.rest.AbstractTestController;
import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.util.StringTestUtils;
import com.example.rest.rest.web.dto.ClientListResponse;
import com.example.rest.rest.web.dto.ClientResponse;
import com.example.rest.rest.web.dto.OrderResponse;
import com.example.rest.rest.web.dto.UpsertClientRequest;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

public class ClientControllerTest extends AbstractTestController {

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    @Test
    public void whenFindAllThenReturnAllClients() throws Exception {

        List<Client> clients = new ArrayList<>();

        createClient(1L, null);
        Order order = createOrder(1L, 100L, null);
        createClient(2L, order);

        List<ClientResponse> clientResponses = new ArrayList<>();
        clientResponses.add(createClientResponse(1L, null));

        OrderResponse orderResponse = createOrderResponse(1L, 100L);
        clientResponses.add(createClientResponse(2l, orderResponse));

        ClientListResponse clientListResponse = new ClientListResponse(clientResponses);

        Mockito.when(clientService.findAll()).thenReturn(clients);
        Mockito.when(clientMapper.clientListToClientResponseList(clients)).thenReturn(clientListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/find_all_clients_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findAll();
        Mockito.verify(clientMapper, Mockito.times(1)).clientListToClientResponseList(clients);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenGetClientByIdThenReturnClientById() throws Exception {

        Client client = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);

        Mockito.when(clientService.getById(1L)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(client)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client/1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/find_client_by_id_response.json");

        Mockito.verify(clientService, Mockito.times(1)).getById(1L);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(client);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateClientThenReturnNewClient() throws Exception {

        Client client = createClient(1L, null);
        client.setName("Client 1");
        // client.setId(1L); //todo выяснить почему id не присваеваетсяж
        Client createdClient = createClient(1L, null);
        ClientResponse clientResponse = createClientResponse(1L, null);
        UpsertClientRequest request = new UpsertClientRequest("Client 1");

        Mockito.when(clientService.save(client)).thenReturn(createdClient);
        Mockito.when(clientMapper.requestToClient(request)).thenReturn(client);
        Mockito.when(clientMapper.clientToResponse(createdClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(post("/api/v1/client").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/create_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).save(client);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(createdClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateClientThenReturnUpdatedClient() throws Exception {

        UpsertClientRequest request = new UpsertClientRequest("New Client 1");
        Client updatedClient = new Client(1L, "New Client 1", new ArrayList<>());
        ClientResponse clientResponse = new ClientResponse(1L, "New Client 1", new ArrayList<>());

        Mockito.when(clientService.update(updatedClient)).thenReturn(updatedClient);
        Mockito.when(clientMapper.requestToClient(1L, request)).thenReturn(updatedClient);
        Mockito.when(clientMapper.clientToResponse(updatedClient)).thenReturn(clientResponse);

        String actualResponse = mockMvc.perform(put("/api/v1/client/1").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("/response/udpate_client_response.json");

        Mockito.verify(clientService, Mockito.times(1)).update(updatedClient);
        Mockito.verify(clientMapper, Mockito.times(1)).requestToClient(1L, request);
        Mockito.verify(clientMapper, Mockito.times(1)).clientToResponse(updatedClient);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteClientByIdThenReturnVoidNoContent() throws Exception {

        mockMvc.perform(delete("/api/v1/client/1")).andExpect(status().isNoContent());
        Mockito.verify(clientService, Mockito.times(1)).deleteById(1L);
    }

//    @Test
//    public void whenFindByIdWhichNotExistaedThenReturnExceptionOrError() throws Exception {
//
//        Mockito.when(clientService.getById(0L)).thenThrow(new ObjectNotFoundException("Object not found 404"));
//
//        var response = mockMvc.perform(get("/api/v1/client/0"))
//                .andExpect(status()
//                        .isNotFound())
//                .andReturn()
//                .getResponse();
//
//        response.setCharacterEncoding("UTF-8");
//
//        String actualResponse = response.getContentAsString();
//        String expectedResponse = StringTestUtils.readStringFromResource("/response/client_by_id_not_found.json");
//
//        Mockito.verify(clientService, Mockito.times(1)).getById(0L);
//
//        Assertions.assertEquals(expectedResponse, actualResponse);
//
//    }
//
//    @Test
//    public void  whenCreateClientWithEmptyNameThenReturnError() throws Exception {
//        var response = mockMvc.perform(post("/api/v1client")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(new UpsertClientRequest())))
//                .andExpect(status().isNotFound())
//                .andReturn()
//                .getResponse();
//
//        response.setCharacterEncoding("UTF-8");
//    }
}
