package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

public class Main {
    void main(){

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductService p = context.getBean(ProductService.class);
//        ProductService p2 = context.getBean(ProductService.class);


        System.out.println(p.getPrice(new Product("", "", BigDecimal.valueOf(100), "")));
//        StandardPricingService standardPricingService = context.getBean(StandardPricingService.class);
//
//        System.out.println(standardPricingService);
//        System.out.println(p == p2);

    }
}
