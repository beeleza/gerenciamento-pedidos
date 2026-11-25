package com.beeleza.print_service.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreatedEvent(
        String eventId,
        String eventType,
        Instant timestamp,
        OrderPayload payload
) {}
