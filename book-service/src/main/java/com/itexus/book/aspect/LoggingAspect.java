package com.itexus.book.aspect;

import com.itexus.book.util.ApplicationContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class LoggingAspect {

    private final MessageSource messageSource;

    @Around("execution(* com.itexus.book.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Locale currentLocale = getCurrentLocale();
//        logger.info(messageSource.getMessage("message.methodCalled", null, currentLocale)
//                + joinPoint.getSignature().toShortString());

        // Логирование параметров
        logMethodParameters(joinPoint);

        Object result;
        try {
            result = joinPoint.proceed(); // Вызов фактического метода
        } catch (Exception e) {
//            logger.error(messageSource.getMessage("message.errorMethod", null, currentLocale), joinPoint.getSignature(), e);
            throw e; // Повторное выбрасывание исключения
        }

        // Логирование возвращаемого значения
//        logger.info(messageSource.getMessage("message.returnValue", null, currentLocale), result);
        return result;
    }

    private void logMethodParameters(ProceedingJoinPoint joinPoint) {
        Locale currentLocale = getCurrentLocale();
        Object[] args = joinPoint.getArgs();
//        logger.info(messageSource.getMessage("message.parameters", null, currentLocale), args);
    }

    private Locale getCurrentLocale() {
        return ApplicationContext.getInstance().getLocale(); // Получение текущей локали
    }
}
