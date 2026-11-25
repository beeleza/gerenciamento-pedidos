package com.beeleza.order_service.dto;

import com.beeleza.order_service.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank String produto,
        @NotNull @Positive Integer quantidade,
        @NotNull @Positive @JsonProperty("valor_unitario") BigDecimal valorUnitario
) {

    public Item toModel() {
        BigDecimal total = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        return new Item(produto, quantidade, valorUnitario, total);
    }
}
