package org.route.service;

import jakarta.annotation.PreDestroy;
import org.route.model.Order;
import org.route.util.MoneyFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory order book. Order numbers look like {@code NM-20261107-00001}.
 */
@Component
public class OrderRegistry {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final AtomicInteger sequence = new AtomicInteger(0);
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public String nextOrderNumber() {
        String date = LocalDate.now().format(DATE_FORMAT);
        return "NM-" + date + "-" + String.format("%05d", sequence.incrementAndGet());
    }

    public void register(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public int orderCount() {
        return orders.size();
    }

    public BigDecimal totalRevenue() {
        return orders.stream()
                .map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PreDestroy
    public void printEndOfDaySummary() {
        System.out.println("End of day: " + orderCount() + " orders, revenue "
                + MoneyFormat.format(totalRevenue()) + " EGP");
    }
}
