package org.route.notification;

import org.route.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender implements NotificationSender {

    @Override
    public void send(Customer customer, String message) {
        System.out.println("[SMS] to " + customer.phone() + ": " + message);
    }
}
