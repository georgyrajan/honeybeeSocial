package com.oracle.survey.usermodule.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class LoggingAspect {

	private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

	@Around(value = "@annotation(com.oracle.survey.usermodule.config.Loggable)", argNames = "joinPoint")
	public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
		Object result = null;
		try {
			MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

			// Get intercepted method details
			String className = methodSignature.getDeclaringType().getSimpleName();
			String methodName = methodSignature.getName();

			final StopWatch stopWatch = new StopWatch();
			// Measure method execution time
			stopWatch.start();
			result = proceedingJoinPoint.proceed();
			stopWatch.stop();
			LOGGER.info("Execution time of %s.%s :: %d ms", className,methodName,stopWatch.getTotalTimeMillis());
		} catch (Exception e) {
			throw e;
		} catch (Throwable e) {
			LOGGER.error("Error occured in Logging Aspect");
		}
		return result;
	}
}
