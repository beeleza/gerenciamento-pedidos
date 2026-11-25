package com.beeleza.processing_service.dto;

import com.beeleza.processing_service.document.ItemDocument;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemResponse(
        String produto,
        Integer quantidade,
        @JsonProperty("valor_unitario") BigDecimal valorUnitario,
        BigDecimal total
) {

    public static ItemResponse from(ItemDocument item) {
        return new ItemResponse(item.produto(), item.quantidade(), item.valorUnitario(), item.total());
    }
}
