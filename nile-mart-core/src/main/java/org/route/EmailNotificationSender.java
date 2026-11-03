package org.route;

public class EmailNotificationSender implements NotificationSender {

    public void sendNotification(Customer customer, String message) {
        if (customer.email() == null) {
            return;
        }

        System.out.println(
                "EMAIL to " + customer.email() + ": " + message
        );
    }

}
