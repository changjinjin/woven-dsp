package com.info.baymax.common.comp.crypto;

import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.method.CryptoMethodInvoker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 接口参数和结果加密解密切面处理
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午11:08:14
 */
@Aspect
@Component
@Slf4j
public class CryptoAspect {

	@Autowired
	private CryptoMethodInvoker cryptoMethodInvoker;

    @Pointcut("@annotation(com.info.baymax.common.crypto.annotation.Cryptoable)")
    public void joinPointExpression() {
    }

    @Around(value = "joinPointExpression()")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        Object[] args = null;

        // 方法
        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        Method method = methodSignature.getMethod();

        Cryptoable cryptoable = method.getAnnotation(Cryptoable.class);
        if (cryptoable == null || !cryptoable.enable()) {
            return pjd.proceed(args);
        }
        try {
            return cryptoMethodInvoker.handleResult(method,
                pjd.proceed(cryptoMethodInvoker.handleArgs(method, pjd.getArgs())));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
