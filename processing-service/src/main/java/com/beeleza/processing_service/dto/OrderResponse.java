package com.beeleza.processing_service.dto;

import com.beeleza.processing_service.document.OrderDocument;
import com.beeleza.processing_service.document.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        List<ItemResponse> itens,
        @JsonProperty("valor_total") BigDecimal valorTotal,
        @JsonProperty("criado_em") Instant criadoEm,
        @JsonProperty("processado_em") Instant processadoEm,
        @JsonProperty("atualizado_em") Instant atualizadoEm
) {

    public static OrderResponse from(OrderDocument order) {
        List<ItemResponse> itens = order.itens() == null
                ? List.of()
                : order.itens().stream().map(ItemResponse::from).toList();
        return new OrderResponse(order.id(), order.currentStatus(), itens, order.valorTotal(),
                order.eventTimestamp(), order.processadoEm(), order.atualizadoEm());
    }
}
