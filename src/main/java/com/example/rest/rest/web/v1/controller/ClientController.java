package com.example.rest.rest.web.v1.controller;

import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.dto.ClientListResponse;
import com.example.rest.rest.web.dto.ClientResponse;
import com.example.rest.rest.web.dto.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    private final ClientMapper mapper;

    @GetMapping
    public ResponseEntity<ClientListResponse> findAll() {
        return ResponseEntity.ok(mapper.clientListToClientResponseList(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.clientToResponse(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody UpsertClientRequest request) {
        Client newClient = mapper.requestToClient(request);
        service.save(newClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.clientToResponse(newClient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable("id") Long clientId,
                                                       @RequestBody UpsertClientRequest request) {

        Client updatedClient = service.update(mapper.requestToClient(clientId, request));
        return ResponseEntity.ok(mapper.clientToResponse(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
