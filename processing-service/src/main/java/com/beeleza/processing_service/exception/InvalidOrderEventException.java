package com.beeleza.processing_service.exception;

public class InvalidOrderEventException extends RuntimeException {

    public InvalidOrderEventException(String eventId, String reason) {
        super("Evento " + eventId + " inválido: " + reason);
    }
}
