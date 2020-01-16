package com.info.baymax.common.comp.serialize.jackson.javassist;//package com.jusfoun.common.message.jackson.javassist;
//
//import java.lang.reflect.Method;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
////@Aspect
//public class IgnorePropertyAspect {
//
//	private static final Logger log = LoggerFactory.getLogger(IgnorePropertyAspect.class);
//
//	private ObjectMapper objectMapper;
//
//	@Pointcut("execution(* com.jusfoun.web.controller.*.*(..))")
//	private void anyMethod() {
//	}
//
//	@Around("anyMethod()")
//	public Object around(ProceedingJoinPoint pjp) throws Throwable {
//		Object returnVal = pjp.proceed(); // 返回源结果
//		try {
//			FilterPropertyHandler filterPropertyHandler = new JavassistFilterPropertyHandler(objectMapper, true);
//			Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//			returnVal = filterPropertyHandler.filterProperties(method, returnVal);
//		} catch (Exception e) {
//			log.error("", e);
//			e.printStackTrace();
//			throw e;
//		}
//		return returnVal;
//	}
//
//	@AfterThrowing(pointcut = "anyMethod()", throwing = "e")
//	public void doAfterThrowing(Exception e) {
//		System.out.println(" -------- AfterThrowing -------- ");
//	}
//}