package org.route.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Single place for displaying money: thousands separators, two decimals,
 * US/English number format regardless of the machine locale.
 */
public final class MoneyFormat {

    private MoneyFormat() {
    }

    public static String format(BigDecimal amount) {
        DecimalFormat format = new DecimalFormat(
                "#,##0.00",
                DecimalFormatSymbols.getInstance(Locale.US));
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(amount);
    }
}
