package com.example.rest.rest.repository.impl;

import com.example.rest.rest.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataBaseClientRepository extends JpaRepository<Client, Long> {

}
