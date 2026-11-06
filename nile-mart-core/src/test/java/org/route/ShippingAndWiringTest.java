package org.route;

import org.junit.jupiter.api.Test;
import org.route.shipping.FreeShippingCalculator;
import org.route.shipping.Governorate;
import org.route.shipping.ShippingQuoteService;
import org.route.shipping.Zone;
import org.route.shipping.ZoneShippingCalculator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShippingAndWiringTest {

    private final ShippingQuoteService quoteService = new ShippingQuoteService(
            new ZoneShippingCalculator(),
            new FreeShippingCalculator(),
            new BigDecimal("5000.00"));

    @Test
    void thereAreExactlyTwentySevenGovernorates() {
        assertEquals(27, Governorate.values().length);
    }

    @Test
    void governoratesAreMappedToTheExpectedZones() {
        assertEquals(Zone.GREATER_CAIRO, Governorate.GIZA.getZone());
        assertEquals(Zone.ALEX_DELTA, Governorate.ALEXANDRIA.getZone());
        assertEquals(Zone.CANAL, Governorate.SUEZ.getZone());
        assertEquals(Zone.UPPER_EGYPT, Governorate.ASWAN.getZone());
        assertEquals(Zone.REMOTE, Governorate.SOUTH_SINAI.getZone());
    }

    @Test
    void belowThresholdPaysZoneFee() {
        assertMoney("50.00", quoteService.quote(Governorate.GIZA, new BigDecimal("4350.00")));
    }

    @Test
    void atThresholdEligibleZoneIsFree() {
        assertMoney("0.00", quoteService.quote(Governorate.ALEXANDRIA, new BigDecimal("5000.00")));
    }

    @Test
    void highSubtotalDoesNotMakeRemoteZoneFree() {
        assertMoney("120.00", quoteService.quote(Governorate.SOUTH_SINAI, new BigDecimal("99999.00")));
    }

    @Test
    void javaFactoryProducesTheSameQuote() {
        ShippingQuoteService service = ObjectFactory.createShippingQuoteService();
        assertMoney("50.00", service.quote(Governorate.GIZA, new BigDecimal("4350.00")));
    }

    @Test
    void xmlWiringProducesTheSameQuote() {
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("beans.xml")) {
            ShippingQuoteService service = context.getBean("shippingQuoteService", ShippingQuoteService.class);
            assertMoney("50.00", service.quote(Governorate.GIZA, new BigDecimal("4350.00")));
        }
    }

    private static void assertMoney(String expected, BigDecimal actual) {
        assertEquals(new BigDecimal(expected), actual);
    }
}
