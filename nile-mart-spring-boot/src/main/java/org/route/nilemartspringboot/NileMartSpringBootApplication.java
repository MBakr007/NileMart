package org.route.nilemartspringboot;

import org.route.nilemartspringboot.config.StoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableConfigurationProperties({StoreProperties.class})
@ConfigurationPropertiesScan
public class NileMartSpringBootApplication {

    void main() {
        SpringApplication.run(NileMartSpringBootApplication.class);
    }
}
