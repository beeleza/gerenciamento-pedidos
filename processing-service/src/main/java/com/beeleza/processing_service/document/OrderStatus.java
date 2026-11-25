package com.beeleza.processing_service.document;

import java.util.Set;

public enum OrderStatus {

    PROCESSADO,
    EM_SEPARACAO,
    ENVIADO,
    ENTREGUE,
    CANCELADO;

    public Set<OrderStatus> nextAllowed() {
        return switch (this) {
            case PROCESSADO -> Set.of(EM_SEPARACAO, CANCELADO);
            case EM_SEPARACAO -> Set.of(ENVIADO, CANCELADO);
            case ENVIADO -> Set.of(ENTREGUE);
            case ENTREGUE, CANCELADO -> Set.of();
        };
    }

    public boolean canTransitionTo(OrderStatus target) {
        return nextAllowed().contains(target);
    }
}
