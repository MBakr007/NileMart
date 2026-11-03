package org.route;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRegistry {

    private final List<Order> orders = new ArrayList<>();
    private int counter = 0;

    public String generateOrderNumber() {

        //increasing the order counter
        counter++;

        //getting today date
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //creating the order number
        return "NM-" + date + "-" + String.format("%05d", counter);
    }

    public void save(Order order) {

        //saving the order in the list
        orders.add(order);
    }

    @PreDestroy
    public void endOfDay() {

        BigDecimal revenue = BigDecimal.ZERO;

        //calculating all orders revenue
        for (Order order : orders) {
            revenue = revenue.add(order.total());
        }

        System.out.println(
                "End of day: " +
                        orders.size() +
                        " orders, revenue " +
                        revenue +
                        " EGP"
        );
    }
}