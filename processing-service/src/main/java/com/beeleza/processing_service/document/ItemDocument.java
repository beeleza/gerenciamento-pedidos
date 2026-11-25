package com.beeleza.processing_service.document;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

public record ItemDocument(
        String produto,
        Integer quantidade,
        @Field(targetType = FieldType.DECIMAL128) BigDecimal valorUnitario,
        @Field(targetType = FieldType.DECIMAL128) BigDecimal total
) {}
