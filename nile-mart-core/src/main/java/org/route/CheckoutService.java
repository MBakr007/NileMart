package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;



    public void placeOrder(ShoppingCart cart, Governorate governorate){
        if(cart.isEmpty()){
            throw  new StoreException("Cart is empty.");
        }

        for(var lineEntry: cart.getLines().entrySet()){
            Product product=productService.findBySku(lineEntry.getKey());
            if(lineEntry.getValue()>product.getStockQty()){
                throw new StoreException("Insufficient stock");
            }
        }
        BigDecimal subtotal=cart.subtotal();
        BigDecimal vat=taxCalculator.calculateVat(subtotal);
        BigDecimal shippingFee=shippingQuoteService.quote(governorate,subtotal);
        BigDecimal total=subtotal.add(vat).add(shippingFee).setScale(2, RoundingMode.HALF_UP);

//        System.out.println("subtotal: "+subtotal);
//        System.out.println("vat: "+vat);
//        System.out.println("shippingfee: "+shippingFee);
//        System.out.println("total: "+total);
        //reduce stock
        for(var lineEntry: cart.getLines().entrySet()){
            Product product=productService.findBySku(lineEntry.getKey());
            productService.reduceStock(product.getSku(),lineEntry.getValue());
        }
        String orderNumber=orderRegistry.generateOrderNumber();
        Order order=new Order(orderNumber,cart.getCustomer(),governorate,cart.getLines(),
                subtotal,vat,shippingFee,total);
        orderRegistry.saveOrder(order);
        for(var sender:notificationSenders){
            sender.send(cart.getCustomer(),"Order "+orderNumber+" confirmed. "+
                    "Total "+total+" EGP");
        }
        cart.clear();
    }
}
