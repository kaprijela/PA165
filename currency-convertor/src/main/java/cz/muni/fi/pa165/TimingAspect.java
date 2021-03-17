package cz.muni.fi.pa165;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * aspect adapted from
 * https://docs.spring.io/spring-framework/docs/4.3.15.RELEASE/spring-framework-reference/html/aop.html#aop-schema-params
 */
@Component
@Aspect
public class TimingAspect {

    @Around("execution(public * *(..))")
    public Object timer(ProceedingJoinPoint call) throws Throwable {
        StopWatch clock = new StopWatch("Timing " + call.toShortString());
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            System.out.println(clock.prettyPrint());
        }
    }
}
