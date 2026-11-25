package com.beeleza.order_service.publisher;

import com.beeleza.order_service.event.OrderCreatedEvent;

public interface OrderEventPublisher {

    void publish(OrderCreatedEvent event);
}
