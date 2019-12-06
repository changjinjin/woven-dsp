package com.merce.woven.common.comp.crypto;

import com.merce.woven.common.crypto.delegater.DefaultCryptorDelegater;
import com.merce.woven.common.crypto.method.AbstractCryptoMethodInvoker;
import com.merce.woven.common.jpa.page.Page;
import com.merce.woven.common.message.result.Response;
import org.springframework.stereotype.Component;

/**
 * 自定义的方法级别参数和返回值加解密调用器
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午3:44:21
 */
@Component
public class CustomCryptoMethodInvoker extends AbstractCryptoMethodInvoker {

    public CustomCryptoMethodInvoker() {
        super(new DefaultCryptorDelegater());
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

        if (obj instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<Object> page = (com.github.pagehelper.Page<Object>) obj;
            obj = page.getResult();
        }
        return obj;
    }

}
