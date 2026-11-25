package com.beeleza.print_service.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemPayload(
        String produto,
        Integer quantidade,
        @JsonProperty("valor_unitario") BigDecimal valorUnitario,
        BigDecimal total
) {}
