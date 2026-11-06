package org.route.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("org.route")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    /**
     * Resolves {@code ${...}} placeholders in {@code @Value} expressions.
     * Must be static so Spring can create it before other beans are instantiated.
     */
    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
