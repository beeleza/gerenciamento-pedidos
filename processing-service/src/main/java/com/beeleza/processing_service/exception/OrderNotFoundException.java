package com.beeleza.processing_service.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Pedido " + id + " não encontrado");
    }
}
