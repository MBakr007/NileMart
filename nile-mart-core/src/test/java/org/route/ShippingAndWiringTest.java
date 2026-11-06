package org.route;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.route.shipping.FreeShippingCalculator;
import org.route.shipping.Governorate;
import org.route.shipping.ShippingQuoteService;
import org.route.shipping.Zone;
import org.route.shipping.ZoneShippingCalculator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.stream.Stream;

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

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("governorateSpecification")
    void everyGovernorateHasTheSpecifiedZoneFeeAndEligibility(Governorate governorate,
                                                              String displayName,
                                                              Zone expectedZone,
                                                              String expectedFee,
                                                              boolean expectedEligibility) {
        assertEquals(displayName, governorate.getDisplayName());
        assertEquals(expectedZone, governorate.getZone());
        assertMoney(expectedFee, governorate.getZone().getFee());
        assertEquals(expectedEligibility, governorate.getZone().isFreeShippingEligible());
    }

    private static Stream<Arguments> governorateSpecification() {
        return Stream.of(
                // GREATER_CAIRO: 50 EGP, free shipping eligible
                Arguments.of(Governorate.CAIRO, "Cairo", Zone.GREATER_CAIRO, "50.00", true),
                Arguments.of(Governorate.GIZA, "Giza", Zone.GREATER_CAIRO, "50.00", true),
                Arguments.of(Governorate.QALYUBIA, "Qalyubia", Zone.GREATER_CAIRO, "50.00", true),

                // ALEX_DELTA: 65 EGP, free shipping eligible
                Arguments.of(Governorate.ALEXANDRIA, "Alexandria", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.BEHEIRA, "Beheira", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.KAFR_EL_SHEIKH, "Kafr El Sheikh", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.DAKAHLIA, "Dakahlia", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.DAMIETTA, "Damietta", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.GHARBIA, "Gharbia", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.MONUFIA, "Monufia", Zone.ALEX_DELTA, "65.00", true),
                Arguments.of(Governorate.SHARQIA, "Sharqia", Zone.ALEX_DELTA, "65.00", true),

                // CANAL: 70 EGP, free shipping eligible
                Arguments.of(Governorate.PORT_SAID, "Port Said", Zone.CANAL, "70.00", true),
                Arguments.of(Governorate.ISMAILIA, "Ismailia", Zone.CANAL, "70.00", true),
                Arguments.of(Governorate.SUEZ, "Suez", Zone.CANAL, "70.00", true),

                // UPPER_EGYPT: 85 EGP, free shipping eligible
                Arguments.of(Governorate.FAIYUM, "Faiyum", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.BENI_SUEF, "Beni Suef", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.MINYA, "Minya", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.ASYUT, "Asyut", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.SOHAG, "Sohag", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.QENA, "Qena", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.LUXOR, "Luxor", Zone.UPPER_EGYPT, "85.00", true),
                Arguments.of(Governorate.ASWAN, "Aswan", Zone.UPPER_EGYPT, "85.00", true),

                // REMOTE: 120 EGP, never free shipping
                Arguments.of(Governorate.MATROUH, "Matrouh", Zone.REMOTE, "120.00", false),
                Arguments.of(Governorate.NORTH_SINAI, "North Sinai", Zone.REMOTE, "120.00", false),
                Arguments.of(Governorate.SOUTH_SINAI, "South Sinai", Zone.REMOTE, "120.00", false),
                Arguments.of(Governorate.RED_SEA, "Red Sea", Zone.REMOTE, "120.00", false),
                Arguments.of(Governorate.NEW_VALLEY, "New Valley", Zone.REMOTE, "120.00", false));
    }

    @Test
    void belowThresholdPaysZoneFee() {
        assertMoney("50.00", quoteService.quote(Governorate.GIZA, new BigDecimal("4350.00")));
    }

    @Test
    void exactlyAtThresholdIsFreeForEligibleZone() {
        assertMoney("0.00", quoteService.quote(Governorate.GIZA, new BigDecimal("5000.00")));
    }

    @Test
    void justBelowThresholdPaysZoneFee() {
        assertMoney("50.00", quoteService.quote(Governorate.GIZA, new BigDecimal("4999.99")));
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
