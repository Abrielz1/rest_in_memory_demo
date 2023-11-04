package com.example.rest.rest.service.impl;

import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.repository.ClientRepository;
import com.example.rest.rest.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.getById(id).orElseThrow(
                () -> new ObjectNotFoundException(MessageFormat.format("такого с id {0} клиента нету", id))
        );
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }
}
