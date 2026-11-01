package org.route;

public class SmsNotificationSender implements NotificationSender{
    @Override
    public void send(Customer customer, String message) {
        System.out.println("SMS to "+customer.phone()+" : "+message);
    }
}
