package view;

import modal.Product;

import java.util.List;

public class ProductViewer {

    public void printProducts(List<Product> products) {
        for (Product product : products) {
            System.out.println("== " + product.getName() + " -- $" + product.getPrice() + " ==");
        }
    }
}
