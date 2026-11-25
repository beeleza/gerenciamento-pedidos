package com.beeleza.print_service.service;

import com.beeleza.print_service.event.ItemPayload;
import com.beeleza.print_service.event.OrderCreatedEvent;
import com.beeleza.print_service.event.OrderPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderPrinter {

    private static final Logger log = LoggerFactory.getLogger(OrderPrinter.class);
    private static final String LINE = "=".repeat(60);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    public void print(OrderCreatedEvent event) {
        OrderPayload order = event.payload();
        if (order == null) {
            log.warn("Evento {} recebido sem dados do pedido", event.eventId());
            return;
        }

        List<ItemPayload> items = Optional.ofNullable(order.items()).orElse(List.of());

        StringBuilder sb = new StringBuilder("\n").append(LINE).append('\n');
        sb.append(String.format(" PEDIDO #%d%n", order.id()));
        sb.append(String.format(" Evento: %s (%s)%n", event.eventId(), event.eventType()));
        if (event.timestamp() != null) {
            sb.append(String.format(" Criado em: %s%n", DATE_FORMAT.format(event.timestamp())));
        }
        sb.append(LINE).append('\n');
        sb.append(String.format(" %-28s %5s %11s %11s%n", "Produto", "Qtd", "Unitário", "Total"));

        BigDecimal orderTotal = BigDecimal.ZERO;
        for (ItemPayload item : items) {
            BigDecimal itemTotal = itemTotal(item);
            orderTotal = orderTotal.add(itemTotal);
            sb.append(String.format(" %-28s %5d %11.2f %11.2f%n",
                    truncate(item.produto(), 28), item.quantidade(), item.valorUnitario(), itemTotal));
        }

        sb.append("-".repeat(60)).append('\n');
        sb.append(String.format(" %-46s %11.2f%n", "TOTAL DO PEDIDO (" + items.size() + (items.size() == 1 ? " item)" : " itens)"), orderTotal));
        sb.append(LINE);

        log.info(sb.toString());
    }

    private BigDecimal itemTotal(ItemPayload item) {
        if (item.total() != null) {
            return item.total();
        }
        if (item.valorUnitario() != null && item.quantidade() != null) {
            return item.valorUnitario().multiply(BigDecimal.valueOf(item.quantidade()));
        }
        return BigDecimal.ZERO;
    }

    private String truncate(String text, int max) {
        if (text == null) {
            return "-";
        }
        return text.length() <= max ? text : text.substring(0, max - 1) + "…";
    }
}
