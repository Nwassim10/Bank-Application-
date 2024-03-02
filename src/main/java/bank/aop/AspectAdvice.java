package bank.aop;

import bank.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;

@Aspect
@Configuration
public class AspectAdvice {
    @Autowired
    private Logger logger;
    @After("execution(* bank.dao.AccountDAO.*(..))")
    public void accountDAOLog(JoinPoint joinPoint){
        String logMessage = "Log for AccountDAO" + " method=" + joinPoint.getSignature().getName();
        logger.log(logMessage);
        System.out.println(logMessage);
    }

    @Around("execution(* bank.service.*.*(..))")
    public Object invoke(ProceedingJoinPoint call ) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start(call.getSignature().getName());
        Object retVal = call.proceed();
        sw.stop();

        long totaltime = sw.getLastTaskTimeMillis();
        System.out.println("Total time to execute=" + totaltime);

        return retVal;
    }

    @After("execution(* bank.jms.JMSSender.sendJMSMessage(..))")
    public void JMSLog(JoinPoint joinPoint){
        String logMessage = "Log for JMS" + " method=" + joinPoint.getSignature().getName();
        logger.log(logMessage);
        System.out.println(logMessage);
    }
}
