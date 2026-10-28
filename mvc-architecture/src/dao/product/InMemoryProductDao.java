package dao.product;

import modal.Product;

import java.util.ArrayList;
import java.util.List;


public class InMemoryProductDao implements ProductDao {

    List<Product> myProducts = new ArrayList<>();

    public InMemoryProductDao() {
        Product p1 = new Product("P001", "Desk Lamp", 230, 10);
        Product p2 = new Product("P002", "Office Chair", 1000,0);
        Product p3 = new Product("P003", "Notebook", 50, 100);

        myProducts.add(p1);
        myProducts.add(p2);
        myProducts.add(p3);
    }


    @Override
    public List<Product> findAll() {
        return myProducts;
    }

    @Override
    public Product findById(String id) {
       for(Product p : myProducts) {
           if(p.getId().equals(id)){
               return p;
           }
       }
       return null;
    }
}
