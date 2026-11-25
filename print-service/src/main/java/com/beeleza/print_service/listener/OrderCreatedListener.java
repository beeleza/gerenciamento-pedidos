package com.beeleza.print_service.listener;

import com.beeleza.print_service.event.OrderCreatedEvent;
import com.beeleza.print_service.service.OrderPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderPrinter orderPrinter;

    public OrderCreatedListener(OrderPrinter orderPrinter) {
        this.orderPrinter = orderPrinter;
    }

    @KafkaListener(topics = "${app.kafka.topics.order-created}")
    public void onOrderCreated(@Payload OrderCreatedEvent event,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Recebido evento {} partição={} offset={}", event.eventId(), partition, offset);
        orderPrinter.print(event);
    }
}
