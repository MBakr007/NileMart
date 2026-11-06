package org.route.notification;

import org.route.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(Customer customer, String message) {
        String email = customer.email();
        if (email == null || email.isBlank()) {
            return;
        }
        System.out.println("[EMAIL] to " + email + ": " + message);
    }
}
