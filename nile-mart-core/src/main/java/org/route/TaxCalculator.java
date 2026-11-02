package org.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class TaxCalculator {
    private final BigDecimal vat;

    public TaxCalculator(@Value("${store.vat-rate}") BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal calculateVat(BigDecimal subtotal){
        return subtotal.multiply(vat).setScale(2, RoundingMode.HALF_UP);
    }
}
