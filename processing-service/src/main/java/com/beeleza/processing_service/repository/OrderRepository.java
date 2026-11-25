package com.beeleza.processing_service.repository;

import com.beeleza.processing_service.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderDocument, Long> {
    boolean existsByEventId(String eventId);
}
