package org.route.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Measures every public method of every class whose simple name ends in
 * "Service" (ProductService, CheckoutService, ShippingQuoteService, ...).
 */
@Component
@Aspect
public class ExecutionLog {

    @Around("execution(* org.route..*Service.*(..))")
    public Object executeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String operation = joinPoint.getSignature().toShortString();
        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            print(operation, start, "Success");
            return result;
        } catch (Throwable error) {
            print(operation, start, "Failed");
            throw error;
        }
    }

    private void print(String operation, long start, String outcome) {
        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("[ExecutionLog] " + operation + " " + outcome
                + " in " + elapsedMillis + " ms");
    }
}
