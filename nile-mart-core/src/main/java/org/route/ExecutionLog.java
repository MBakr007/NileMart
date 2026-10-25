package org.route;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionLog {

    @Around("execution(* org.route.ProductService.*(..)))")
    public Object executeLog(ProceedingJoinPoint jp) throws Throwable {
        String op = jp.getSignature().toShortString();
        System.out.println(op + " Started");

        try {
            Object result = jp.proceed();
            System.out.println(op + " Success");
            return result;
        } catch (Throwable e) {
            System.out.println(op + " Failed");
            throw e;
        }
    }

//    @Before("execution(* org.route.ProductService.*(..))")
//    public void executeBefore() {
//        System.out.println("before");
//    }
}
