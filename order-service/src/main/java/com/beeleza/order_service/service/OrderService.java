package com.beeleza.order_service.service;

import com.beeleza.order_service.dto.OrderRequest;
import com.beeleza.order_service.event.OrderCreatedEvent;
import com.beeleza.order_service.model.Order;
import com.beeleza.order_service.publisher.OrderEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public OrderCreatedEvent createOrder(OrderRequest request) {
        Order order = request.toModel();
        OrderCreatedEvent event = OrderCreatedEvent.of(order);
        eventPublisher.publish(event);
        return event;
    }
}
