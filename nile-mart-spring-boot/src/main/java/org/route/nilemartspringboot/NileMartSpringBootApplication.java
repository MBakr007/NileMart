package org.route.nilemartspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NileMartSpringBootApplication {

    void main() {
        SpringApplication.run(NileMartSpringBootApplication.class);
    }
}
