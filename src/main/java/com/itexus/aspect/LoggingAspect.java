package com.itexus.aspect;

import com.itexus.controller.BookController;
import com.itexus.util.ApplicationContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private  MessageSource messageSource;

    public LoggingAspect(BookController controller) {
        this.messageSource = controller.getMessageSource();
    }

    @After("execution(* com.itexus.service..*(..))")
    public void logAfter(JoinPoint joinPoint) {
        Locale currentLocale = ApplicationContext.getInstance().getLocale();
        logger.info(messageSource.getMessage("message.methodCalled", null, currentLocale)
                + joinPoint.getSignature().toShortString());
    }
}
