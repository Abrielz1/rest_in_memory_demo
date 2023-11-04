package com.example.rest.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Order {

    private Long id;

    private String product;

    private BigDecimal cost;

    Client client;

    private Instant createAt;

    private Instant updateAt;
}
