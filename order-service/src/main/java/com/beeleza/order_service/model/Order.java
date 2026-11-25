package com.beeleza.order_service.model;

import com.beeleza.order_service.exception.InvalidOrderException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Order {

    private Long id;
    private List<Item> items = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, List<Item> items) {
        validateItems(items);
        this.id = id;
        this.items = items;
    }

    private static void validateItems(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("O pedido deve conter ao menos um item");
        }
        Set<String> produtos = new HashSet<>();
        for (Item item : items) {
            if (!produtos.add(item.getProduto().trim().toLowerCase())) {
                throw new InvalidOrderException(
                        "Produto '" + item.getProduto() + "' repetido no pedido; agrupe as quantidades em um único item");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
