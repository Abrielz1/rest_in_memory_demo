package com.example.rest.rest.service.impl;

import com.example.rest.rest.exceptions.ObjectNotFoundException;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.repository.impl.DataBaseClientRepository;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataBaseClientService implements ClientService {

    private final DataBaseClientRepository repository;

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        MessageFormat.format("Client with id: {0} is not found in db!", id)));
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Client client) {

        Long id = client.getId();

        Client clientFDB = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                MessageFormat.format("Client with id: {0} is not found in db!", id)));

        BeanUtils.copyNonNullProperties(client, clientFDB);

        return repository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
