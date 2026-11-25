package com.beeleza.order_service.event;

import com.beeleza.order_service.model.Order;

import java.util.List;

public record OrderPayload(
        Long id,
        List<ItemPayload> items
) {

    public static OrderPayload from(Order order) {
        return new OrderPayload(order.getId(), order.getItems().stream().map(ItemPayload::from).toList());
    }
}
