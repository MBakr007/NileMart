package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService{

    private final ProductRepository repository;
    private final PricingService pricingService;

    public BigDecimal getPrice(String sku, int qty) {
        Product product = repository.findBySku(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new StoreException("Product isn't available."));
        return pricingService.getPrice(product, qty);
    }

    public List<Product>findAll(){
        return repository.findAll();
    }

    public Product findBySku(String sku){
        return repository.findBySku(sku).orElseThrow(()-> new StoreException("the product u searched for, is not found."));
    }

    public void reduceStock(String sku, int qty){
        //getting the product
        Product p = findBySku(sku);

        if (qty <= 0 ){
            throw new StoreException("qty is invalid. it must be greater than 0.");
        }

        //checking if the qty of the product is more than the required
        if(p.getStockQuantity() < qty ){
            throw new StoreException("the product is not in stock. product sku: " + sku );
        }

        //updating the avaiable qty
        p.setStockQuantity(p.getStockQuantity() - qty);

        //saving the product.
        repository.save(p);
    }





}
