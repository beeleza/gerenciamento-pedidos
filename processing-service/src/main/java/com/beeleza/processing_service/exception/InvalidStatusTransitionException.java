package com.beeleza.processing_service.exception;

import com.beeleza.processing_service.document.OrderStatus;

import java.util.Set;

public class InvalidStatusTransitionException extends RuntimeException {

    private final OrderStatus current;
    private final OrderStatus requested;

    public InvalidStatusTransitionException(Long orderId, OrderStatus current, OrderStatus requested) {
        super("Pedido " + orderId + " não pode mudar de " + current + " para " + requested);
        this.current = current;
        this.requested = requested;
    }

    public OrderStatus getCurrent() {
        return current;
    }

    public OrderStatus getRequested() {
        return requested;
    }

    public Set<OrderStatus> getAllowed() {
        return current.nextAllowed();
    }
}
