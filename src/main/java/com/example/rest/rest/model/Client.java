package com.example.rest.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private Long id;

    private String name;

    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Long id) {
       orders = orders.stream()
                .filter(i -> !i.getId().equals(id))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }
}
