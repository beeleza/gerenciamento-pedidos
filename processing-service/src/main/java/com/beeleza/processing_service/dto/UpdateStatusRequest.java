package com.beeleza.processing_service.dto;

import com.beeleza.processing_service.document.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "é obrigatório") OrderStatus status
) {}
