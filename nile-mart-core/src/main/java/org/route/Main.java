package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

public class Main {
    void main(){

        //        var context = new ClassPathXmlApplicationContext("beans.xml");


//        StandardPricingService standard = context.getBean(StandardPricingService.class);
//        StandardPricingService standard2 = context.getBean(StandardPricingService.class);

//        ProductService p = context.getBean(ProductService.class);
//
//        System.out.println(p);

        var context = new AnnotationConfigApplicationContext(AppConfig.class);
//        var context2 = new AnnotationConfigApplicationContext(AppConfig.class);

//        StandardPricingService standard = context.getBean(StandardPricingService.class);
//        StandardPricingService standard2 = context2.getBean(StandardPricingService.class);

//        System.out.println(standard == standard2);
//        ProductService p = context.getBean(ProductService.class);
//        System.out.println(standard);

        ProductService productService = context.getBean(ProductService.class);

        System.out.println(productService);

        Product product = Product.builder()
                .price(BigDecimal.valueOf(100))
                .build();

        System.out.println(productService.getPrice("sku", 2));

    }
}
