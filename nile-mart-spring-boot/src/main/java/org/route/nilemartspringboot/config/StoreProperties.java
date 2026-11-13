package org.route.nilemartspringboot.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "store")
public class StoreProperties {
    BigDecimal vatRate;
}
