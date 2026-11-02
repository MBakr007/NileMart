package org.route;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Component
@Scope("prototype")
public class ShoppingCart {
    private final String cartNumber;
    private final Customer customer;
    @Getter(AccessLevel.NONE)   // prevent lombok from creating a getter for this field
    private final Map<String,Integer> lines; // SKU, qty
    @Getter(AccessLevel.NONE)
    private final  ProductService productService;
    @Getter(AccessLevel.NONE)
    private final PricingService pricingService;
    private final int maxQtyPerProduct;

    public ShoppingCart(String cartNumber, Customer customer,
                        ProductService productService,PricingService pricingService, @Value("${store.max-quantity-per-product}") int maxQtyPerProduct) {
        this.cartNumber = cartNumber;
        this.customer = customer;
        this.productService = productService;
        this.pricingService=pricingService;
        this.maxQtyPerProduct = maxQtyPerProduct;
        this.lines = new HashMap<>();
    }

    public void addItem(String sku, int qty){
       Product product= productService.findBySku(sku);

        if (qty <= 0) {
            throw new StoreException("Quantity must be positive.");
        }

       int productStockQty = product.getStockQty();
       if(productStockQty==0 )
           throw new StoreException("The product's stock is 0.");

       int currentQty=lines.getOrDefault(sku,0);
       int newQty=currentQty+qty;
       if(newQty>maxQtyPerProduct)
           throw new StoreException("Max quantity per product is "+maxQtyPerProduct);

       if (newQty > productStockQty) {
            throw new StoreException("Insufficient stock.");
       }

       lines.put(sku,newQty);
    }

    public BigDecimal subtotal(){
        BigDecimal total=BigDecimal.ZERO;
        for(var entry:lines.entrySet()){
            Product product=productService.findBySku(entry.getKey());
            total=total.add(pricingService.getPrice(product,entry.getValue()));
        }
        return total;
    }
    public boolean isEmpty(){
        return lines.isEmpty();
    }
    public void clear(){
        lines.clear();
    }
    public Map<String, Integer> getLines() {
        return new HashMap<>(lines);
    }
}
