package com.info.baymax.common.comp.utils;

import javassist.util.proxy.ProxyFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 代理工具类
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午5:28:28
 */
public class ProxyTargetUtils {

    /**
     * 获取代理对象的原始类型
     *
     * @throws Exception
     */
    public static Class<?> getTargetClass(Object proxy) throws Exception {
        if (AopUtils.isAopProxy(proxy)) {
            return AopUtils.getTargetClass(proxy);
        } else if (ProxyFactory.isProxyClass(proxy.getClass())) {
            return proxy.getClass().getSuperclass();
        } else {
            return proxy.getClass();
        }
    }

    /**
     * 说明：获取代理对象的原来的类型对象. <br>
     *
     * @param proxy 代理对象
     * @return
     * @throws Exception
     * @author yjw@jusfoun.com
     * @date 2017年12月8日 上午10:32:41
     */
    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * 说明： 获取CGLIB代理的对象真实对象. <br>
     *
     * @param proxy 代理对象
     * @return 真实类型对象
     * @throws Exception
     * @author yjw@jusfoun.com
     * @date 2017年12月8日 上午10:33:37
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }

    /**
     * 说明： 获取JDK动态代理的真实对象. <br>
     *
     * @param proxy 代理对象
     * @return 真实对象
     * @throws Exception
     * @author yjw@jusfoun.com
     * @date 2017年12月8日 上午10:34:38
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }
}
