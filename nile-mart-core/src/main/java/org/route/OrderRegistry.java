package org.route;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRegistry {
    private final List<Order>orders;
    private int orderCounter=0;
    public OrderRegistry() {
        this.orders = new ArrayList<>();
    }
    public String generateOrderNumber(){
        orderCounter++;
        return "NM-"+ LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)+
                "-"+String.format("%05d",orderCounter);
    }
    public void saveOrder(Order order){
        orders.add(order);
    }
    private BigDecimal calculateRevenue(){
        BigDecimal revenue=BigDecimal.ZERO;
        for(Order order:orders){
            revenue=revenue.add(order.total());
        }
        return revenue.setScale(2, RoundingMode.HALF_UP);
    }
    @PreDestroy
    public void printDaySummary(){
        System.out.println("End of day: "+orders.size()+" orders, revenue "+calculateRevenue()+" EGP");
    }

}
