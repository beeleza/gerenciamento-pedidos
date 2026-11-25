package com.beeleza.order_service.event;

import com.beeleza.order_service.model.Order;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        String eventId,
        String eventType,
        Instant timestamp,
        OrderPayload payload
) {

    public static OrderCreatedEvent of(Order order) {
        return new OrderCreatedEvent(UUID.randomUUID().toString(), "ORDER_CREATED", Instant.now(), OrderPayload.from(order));
    }
}
