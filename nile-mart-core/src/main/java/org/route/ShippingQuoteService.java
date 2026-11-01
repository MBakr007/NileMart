package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {
    private final ShippingCalculator primaryCalculator;
    private final ShippingCalculator freeCalculator;
    private final BigDecimal threshold;


    public ShippingQuoteService(ShippingCalculator primaryCalculator,
                                @Qualifier("freeShipping") ShippingCalculator freeCalculator,
                                @Value("${store.free-shipping-threshold}") BigDecimal threshold) {
        this.primaryCalculator = primaryCalculator;
        this.freeCalculator = freeCalculator;
        this.threshold = threshold;
    }

    public BigDecimal quote(Governorate governorate, BigDecimal subtotal){
        if(subtotal.compareTo(threshold)>=0 && governorate.getZone().isFreeShippingFlag()) {
            return freeCalculator.calculate(governorate);
        }

        return primaryCalculator.calculate(governorate);
    }
}
