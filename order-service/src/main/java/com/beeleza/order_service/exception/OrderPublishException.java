package com.beeleza.order_service.exception;

public class OrderPublishException extends RuntimeException {

    private final Long orderId;

    public OrderPublishException(Long orderId, Throwable cause) {
        super("Não foi possível publicar o pedido " + orderId + " no Kafka", cause);
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
