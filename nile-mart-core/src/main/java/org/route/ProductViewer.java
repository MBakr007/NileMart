package org.route;




import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ProductViewer {

    public void printProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println("== " + product.getName() + " -- $" + product.getPrice() + " ==");
        }
    }
}
