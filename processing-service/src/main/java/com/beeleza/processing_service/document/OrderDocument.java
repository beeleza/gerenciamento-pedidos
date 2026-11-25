package com.beeleza.processing_service.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document
public record OrderDocument(
        @Id Long id,
        @Indexed(unique = true) String eventId,
        String eventType,
        Instant eventTimestamp,
        List<ItemDocument> itens,
        @Field(targetType = FieldType.DECIMAL128) BigDecimal valorTotal,
        Instant processadoEm,
        OrderStatus status,
        Instant atualizadoEm
) {

    /** Pedidos gravados antes do campo status existir são tratados como PROCESSADO. */
    public OrderStatus currentStatus() {
        return status != null ? status : OrderStatus.PROCESSADO;
    }

    public OrderDocument withStatus(OrderStatus newStatus) {
        return new OrderDocument(id, eventId, eventType, eventTimestamp, itens, valorTotal,
                processadoEm, newStatus, Instant.now());
    }
}
