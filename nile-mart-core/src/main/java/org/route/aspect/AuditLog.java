package org.route.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.route.cart.ShoppingCart;
import org.route.shipping.Governorate;
import org.springframework.stereotype.Component;

/**
 * Traces checkout attempts before any mutation happens.
 */
@Component
@Aspect
public class AuditLog {

    @Before("execution(* org.route.service.CheckoutService.placeOrder(..))")
    public void auditCheckout(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length < 2 || !(args[0] instanceof ShoppingCart cart)
                || !(args[1] instanceof Governorate governorate)) {
            return;
        }
        System.out.println("[AuditLog] Checkout started for cart " + cart.getCartNumber()
                + " to " + governorate);
    }
}
