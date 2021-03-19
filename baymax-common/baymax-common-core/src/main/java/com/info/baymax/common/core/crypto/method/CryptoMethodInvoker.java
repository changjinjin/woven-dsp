package com.info.baymax.common.core.crypto.method;

import java.lang.reflect.Method;

/**
 * 根据方法上面的加解密注解进行参数和返回值加解密处理
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午12:01:30
 */
public interface CryptoMethodInvoker {

    /**
     * 对方法参数的加解密处理
     *
     * @param method 方法
     * @param args   未处理的参数列表
     * @return 处理后的参数列表
     * @throws Exception
     */
    Object[] handleArgs(Method method, Object[] args);

    /**
     * 对方法返回值进行加解密处理
     *
     * @param method 方法
     * @param result 未处理的返回值
     * @return 处理后的返回值
     */
    Object handleResult(Method method, Object result);

}
