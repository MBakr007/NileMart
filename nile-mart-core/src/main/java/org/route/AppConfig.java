package org.route;


import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.math.BigDecimal;


@Configuration
@ComponentScan
//@PropertySource("/beans.xml")
@PropertySource("/application.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

//    @Bean
//    @Scope("prototype")
//    ProductService productService(){
//        return new ProductService(inMemoryProductRepository(), standardPricingService());
//    }
//
//    @Bean
//    InMemoryProductRepository inMemoryProductRepository() {
//        return new InMemoryProductRepository();
//    }
//
//    @Bean
//    StandardPricingService standardPricingService() {
//        return new StandardPricingService();
//    }
//
//    @Bean
//    PromotionalPricingService promotionalPricingService() {
//        return new PromotionalPricingService(BigDecimal.valueOf(10));
//    }

}
