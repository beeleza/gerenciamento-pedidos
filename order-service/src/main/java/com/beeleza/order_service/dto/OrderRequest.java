package com.beeleza.order_service.dto;

import com.beeleza.order_service.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
        @NotNull @Positive Long id,
        @NotEmpty List<@NotNull @Valid ItemRequest> items
) {

    public Order toModel() {
        return new Order(id, items.stream().map(ItemRequest::toModel).toList());
    }
}
