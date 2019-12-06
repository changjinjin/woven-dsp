package com.merce.woven.common.crypto.method;

import com.merce.woven.common.crypto.delegater.DefaultCryptorDelegater;

/**
 * 默认的方法参数和返回值加解密调用器
 * 
 * @author jingwei.yang
 * @date 2019年12月6日 下午12:35:46
 */
public class DefaultCryptoMethodInvoker extends AbstractCryptoMethodInvoker {

	public DefaultCryptoMethodInvoker() {
		super(new DefaultCryptorDelegater());
	}

	@Override
	public Object afterHandleResult(Object result, Object obj) {
		return result;
	}

	@Override
	public Object beforeHandleResult(Object result) {
		return result;
	}

}
