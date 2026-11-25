package com.beeleza.print_service.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderPayload(
        Long id,
        List<ItemPayload> items
) {}
