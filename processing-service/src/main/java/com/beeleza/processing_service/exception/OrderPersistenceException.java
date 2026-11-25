package com.beeleza.processing_service.exception;

public class OrderPersistenceException extends RuntimeException {

    public OrderPersistenceException(Long orderId, Throwable cause) {
        super("Falha ao persistir o pedido " + orderId + " no MongoDB", cause);
    }
}
