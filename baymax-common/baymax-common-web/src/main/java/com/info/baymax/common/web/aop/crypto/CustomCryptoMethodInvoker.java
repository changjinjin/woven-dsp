package com.info.baymax.common.web.aop.crypto;

import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.core.crypto.method.AbstractCryptoMethodInvoker;
import com.info.baymax.common.core.crypto.method.CryptoConfig;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.Response;
import com.info.baymax.common.persistence.jpa.page.Page;
import org.springframework.stereotype.Component;

/**
 * 自定义的方法级别参数和返回值加解密调用器
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午3:44:21
 */
@SuppressWarnings("deprecation")
@Component
public class CustomCryptoMethodInvoker extends AbstractCryptoMethodInvoker {

    public CustomCryptoMethodInvoker(final CryptoConfig config, final CryptorDelegater cryptorDelegater) {
        super(config.getSecretKey(), cryptorDelegater);
    }

    @Override
    public Object afterHandleResult(Object result, Object obj) {
        if (result instanceof String) {
            return obj;
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "resource"})
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

        if (obj instanceof Page) {
            Page<Object> page = (Page<Object>) obj;
            obj = page.getContent();
        }

        if (obj instanceof IPage) {
            IPage<Object> page = (IPage<Object>) obj;
            obj = page.getList();
        }

        if (obj instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<Object> page = (com.github.pagehelper.Page<Object>) obj;
            obj = page.getResult();
        }
        return obj;
    }

}
