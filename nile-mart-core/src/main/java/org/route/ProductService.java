package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final PricingService pricingService;

//    public ProductService(@Qualifier("inMemoryProductRepository") ProductRepository repository,
//                          PricingService pricingService) {
//        this.repository = repository;
//        this.pricingService = pricingService;
//    }

    public BigDecimal getPrice(String sku, int qty) {
        Product product = repository.findBySku(sku)
                .filter(Product::isActive)
                .orElseThrow(() -> new StoreException("Product isn't available."));
        return pricingService.getPrice(product, qty);
    }

    public List<Product> findAll(){
        return repository.findAll();
    }

    public Product findBySku(String sku){
        Optional<Product> product=repository.findBySku(sku);
        if(product.isPresent()) {
            return product.get();
        }
        throw new StoreException("No product with this SKU.");
    }

    public void reduceStock(String sku,int qty){
        Product product=findBySku(sku);
        if(product.getStockQty()<qty) {
            throw new StoreException("Insufficient stock.");
        }
        product.setStockQty(product.getStockQty()-qty);
        repository.save(product);
    }
}
