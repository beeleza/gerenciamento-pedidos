package com.beeleza.processing_service.service;

import com.beeleza.processing_service.document.ItemDocument;
import com.beeleza.processing_service.document.OrderDocument;
import com.beeleza.processing_service.document.OrderStatus;
import com.beeleza.processing_service.dto.OrderResponse;
import com.beeleza.processing_service.event.ItemPayload;
import com.beeleza.processing_service.event.OrderCreatedEvent;
import com.beeleza.processing_service.exception.InvalidOrderEventException;
import com.beeleza.processing_service.exception.InvalidStatusTransitionException;
import com.beeleza.processing_service.exception.OrderNotFoundException;
import com.beeleza.processing_service.exception.OrderPersistenceException;
import com.beeleza.processing_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void process(OrderCreatedEvent event) {
        validate(event);

        if (orderRepository.existsByEventId(event.eventId())) {
            log.info("Evento {} já processado, ignorando", event.eventId());
            return;
        }

        List<ItemDocument> itens = event.payload().items().stream()
                .map(this::toItem)
                .toList();

        BigDecimal valorTotal = itens.stream()
                .map(ItemDocument::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderDocument pedido = new OrderDocument(
                event.payload().id(),
                event.eventId(),
                event.eventType(),
                event.timestamp(),
                itens,
                valorTotal,
                Instant.now(),
                OrderStatus.PROCESSADO,
                Instant.now()
        );

        try {
            orderRepository.save(pedido);
        } catch (DuplicateKeyException e) {
            log.info("Evento {} já processado (gravação concorrente), ignorando", event.eventId());
            return;
        } catch (DataAccessException e) {
            throw new OrderPersistenceException(pedido.id(), e);
        }
        log.info("Pedido {} salvo com {} itens", pedido.id(), itens.size());
    }

    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(OrderResponse::from);
    }

    public OrderResponse findById(Long id) {
        return OrderResponse.from(getOrder(id));
    }

    public OrderResponse updateStatus(Long id, OrderStatus newStatus) {
        OrderDocument order = getOrder(id);
        OrderStatus current = order.currentStatus();

        if (!current.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException(id, current, newStatus);
        }

        OrderDocument updated = orderRepository.save(order.withStatus(newStatus));
        log.info("Pedido {} mudou de {} para {}", id, current, newStatus);
        return OrderResponse.from(updated);
    }

    private OrderDocument getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private void validate(OrderCreatedEvent event) {
        if (event.payload() == null || event.payload().id() == null) {
            throw new InvalidOrderEventException(event.eventId(), "payload ou id do pedido ausente");
        }
        if (event.payload().items() == null || event.payload().items().isEmpty()) {
            throw new InvalidOrderEventException(event.eventId(), "pedido sem itens");
        }
        boolean itemInvalido = event.payload().items().stream().anyMatch(i ->
                i == null || i.quantidade() == null || (i.total() == null && i.valorUnitario() == null));
        if (itemInvalido) {
            throw new InvalidOrderEventException(event.eventId(), "item sem quantidade ou valor");
        }
    }

    private ItemDocument toItem(ItemPayload p) {
        BigDecimal total = p.total() != null
                ? p.total()
                : p.valorUnitario().multiply(BigDecimal.valueOf(p.quantidade()));
        return new ItemDocument(p.produto(), p.quantidade(), p.valorUnitario(), total);
    }
}
