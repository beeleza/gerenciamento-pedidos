package com.beeleza.order_service.publisher;

import com.beeleza.order_service.event.OrderCreatedEvent;
import com.beeleza.order_service.exception.OrderPublishException;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    private final String topic;
    private final long sendTimeoutSeconds;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate,
                                    @Value("${app.kafka.topics.order-created}") String topic,
                                    @Value("${app.kafka.send-timeout-seconds:10}") long sendTimeoutSeconds) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.sendTimeoutSeconds = sendTimeoutSeconds;
    }

    @Override
    public void publish(OrderCreatedEvent event) {
        Long orderId = event.payload().id();
        String key = String.valueOf(orderId);

        try {
            RecordMetadata metadata = kafkaTemplate.send(topic, key, event)
                    .get(sendTimeoutSeconds, TimeUnit.SECONDS)
                    .getRecordMetadata();
            log.info("Evento {} do pedido {} publicado em {}-{} offset {}",
                    event.eventId(), orderId, topic, metadata.partition(), metadata.offset());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OrderPublishException(orderId, e);
        } catch (ExecutionException e) {
            throw new OrderPublishException(orderId, e.getCause());
        } catch (TimeoutException | KafkaException e) {
            throw new OrderPublishException(orderId, e);
        }
    }
}
