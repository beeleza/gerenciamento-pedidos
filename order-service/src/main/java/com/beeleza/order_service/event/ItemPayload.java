package com.beeleza.order_service.event;

import com.beeleza.order_service.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemPayload(
        String produto,
        Integer quantidade,
        @JsonProperty("valor_unitario") BigDecimal valorUnitario,
        BigDecimal total
) {

    public static ItemPayload from(Item item) {
        return new ItemPayload(item.getProduto(), item.getQuantidade(), item.getValorUnitario(), item.getTotal());
    }
}
