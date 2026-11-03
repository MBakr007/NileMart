package org.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxCalculator {

    private final BigDecimal vatRate;

    public TaxCalculator(
            @Value("${store.vat-rate}") BigDecimal vatRate
    ) {
        this.vatRate = vatRate;
    }

    public BigDecimal calculate(BigDecimal subtotal) {

        //calculating the vat from subtotal
        return subtotal
                .multiply(vatRate)
                .setScale(2, RoundingMode.HALF_UP);
    }
}