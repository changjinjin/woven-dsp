package com.info.baymax.common.core.crypto.method;

import com.info.baymax.common.core.crypto.delegater.DefaultCryptorDelegater;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.Response;

/**
 * 默认的方法参数和返回值加解密调用器
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午12:35:46
 */
public class DefaultCryptoMethodInvoker extends AbstractCryptoMethodInvoker {

    public DefaultCryptoMethodInvoker(String secretKey) {
        super(secretKey, new DefaultCryptorDelegater());
    }

    @Override
    public Object afterHandleResult(Object result, Object obj) {
        if (result instanceof String) {
            return obj;
        }
        return result;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object beforeHandleResult(Object result) {
        // 拿到返回报文对象的能够处理的层级
        Object obj = null;
        if (result instanceof Response) {
            Response<Object> responseObj = (Response<Object>) result;
            obj = responseObj.getContent();
        } else {
            obj = result;
        }

        if (obj instanceof IPage) {
            IPage<Object> page = (IPage<Object>) obj;
            obj = page.getList();
        }
        return obj;
    }
}
