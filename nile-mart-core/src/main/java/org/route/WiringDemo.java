package org.route;

import org.route.shipping.Governorate;
import org.route.shipping.ShippingQuoteService;
import org.route.util.MoneyFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * Demonstrates that the same graph can be assembled by hand (ObjectFactory)
 * or by Spring from beans.xml, with identical results.
 */
public class WiringDemo {

    public static void main(String[] args) {
        BigDecimal subtotal = new BigDecimal("4350.00");

        ShippingQuoteService fromFactory = ObjectFactory.createShippingQuoteService();
        System.out.println("Java factory (Giza, 4350.00): "
                + MoneyFormat.format(fromFactory.quote(Governorate.GIZA, subtotal)));

        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("beans.xml")) {
            ShippingQuoteService fromXml = context.getBean("shippingQuoteService", ShippingQuoteService.class);
            System.out.println("XML context  (Giza, 4350.00): "
                    + MoneyFormat.format(fromXml.quote(Governorate.GIZA, subtotal)));
        }
    }
}
