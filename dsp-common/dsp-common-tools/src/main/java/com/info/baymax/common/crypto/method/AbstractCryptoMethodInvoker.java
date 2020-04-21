package com.info.baymax.common.crypto.method;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoOperation;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.Cryptoable;
import com.info.baymax.common.crypto.annotation.Decrypt;
import com.info.baymax.common.crypto.annotation.Encrypt;
import com.info.baymax.common.crypto.annotation.ReturnOperation;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;

public abstract class AbstractCryptoMethodInvoker implements CryptoMethodInvoker {

	/**
	 * 全局的secretKey
	 */
	private final String secretKey;

	/**
	 * 加解密的处理器委派对象
	 */
	private final CryptorDelegater cryptorDelegater;

	public AbstractCryptoMethodInvoker(String secretKey, CryptorDelegater cryptorDelegater) {
		this.secretKey = secretKey;
		this.cryptorDelegater = cryptorDelegater;
	}

	public Object[] handleArgs(Method method, Object[] args) {
		Cryptoable cryptoable = method.getAnnotation(Cryptoable.class);
		if (cryptoable == null || !cryptoable.enable()) {
			return args;
		}
		Parameter[] parameters = method.getParameters();
		if (cryptoable.enableParam() && parameters != null && parameters.length > 0) {
			Parameter parameter = null;
			Object obj = null;
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					parameter = parameters[i];
					obj = args[i];

					Encrypt encrypt = parameter.getAnnotation(Encrypt.class);
					if (encrypt != null) {
						obj = encryptObject(obj, encrypt.cryptoType());
					}
					Decrypt decrypt = parameter.getAnnotation(Decrypt.class);
					if (decrypt != null) {
						obj = decryptObject(obj);
					}

					args[i] = obj;
				}
			}
		}
		return args;
	}

	public Object handleResult(Method method, Object result) {
		Cryptoable cryptoable = method.getAnnotation(Cryptoable.class);
		if (cryptoable == null || !cryptoable.enable()) {
			return result;
		}

		if (result != null) {
			// 如果指明了返回需要做处理才进行处理逻辑，否则跳过
			ReturnOperation[] returnOperations = cryptoable.returnOperation();
			if (returnOperations != null && returnOperations.length > 0) {

				// 处理前预处理
				Object obj = beforeHandleResult(result);

				// 开始加解密处理逻辑
				if (obj instanceof String) {
					for (ReturnOperation returnOperation : returnOperations) {
						obj = handleReturnResult(returnOperation, obj);
					}
					result = afterHandleResult(result, obj);
				} else {
					for (ReturnOperation returnOperation : returnOperations) {
						handleReturnResult(returnOperation, obj);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 加解密处理完成后的组装结果
	 * 
	 * @param result 原始返回值
	 * @param obj    提取并处理后的对象
	 * @return 组装后的返回值
	 */
	public abstract Object afterHandleResult(Object result, Object obj);

	/**
	 * 返回值处理前预处理，提取出内部的能够处理的CryptoBean对象，如CryptoBean的单个对象、集合、数组或字符串
	 * 
	 * @param result 未提取的结果
	 * @return 提取的对象
	 */
	public abstract Object beforeHandleResult(Object result);

	/**
	 * 处理方法返回值的加解密逻辑
	 *
	 * @param returnOperation 返回加解密操作描述
	 * @param resultBody      报文对象
	 */
	private Object handleReturnResult(ReturnOperation returnOperation, Object resultBody) {
		CryptoOperation cryptoOperation = returnOperation.cryptoOperation();
		switch (cryptoOperation) {
		case Encrypt:
			return encryptObject(resultBody, returnOperation.cryptoType());
		case Decrypt:
			return decryptObject(resultBody);
		default:
			return resultBody;
		}
	}

	// 处理较复杂对象的加密逻辑
	private Object encryptObject(Object obj, CryptoType cryptoType) {
		Class<? extends Object> clazz = obj.getClass();
		if (Iterable.class.isAssignableFrom(clazz)) {
			Iterator<?> iterator = ((Iterable<?>) obj).iterator();
			iterator.forEachRemaining(t -> {
				encryptSingle(t, cryptoType);
			});
		} else if (clazz.isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				encryptSingle(Array.get(obj, i), cryptoType);
			}
		} else {
			return encryptSingle(obj, cryptoType);
		}
		return obj;
	}

	// 处理单个对象的解密逻辑
	private Object encryptSingle(Object bean, CryptoType cryptoType) {
		if (CryptoBean.class.isAssignableFrom(bean.getClass())) {
			CryptoBean cryptoBean = (CryptoBean) bean;
			cryptoBean.encrypt(secretKey, cryptoType, cryptorDelegater);
			return cryptoBean;
		} else if (bean instanceof String) {
			return cryptorDelegater.encrypt((String) bean, secretKey, cryptoType);
		}
		return bean;
	}

	// 处理较复杂对象的解密逻辑
	private Object decryptObject(Object obj) {
		Class<? extends Object> clazz = obj.getClass();
		if (Iterable.class.isAssignableFrom(clazz)) {
			Iterator<?> iterator = ((Iterable<?>) obj).iterator();
			iterator.forEachRemaining(t -> {
				decryptSingle(t);
			});
		} else if (clazz.isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				decryptSingle(Array.get(obj, i));
			}
		} else {
			return decryptSingle(obj);
		}
		return obj;
	}

	// 处理单个对象的加密逻辑
	private Object decryptSingle(Object bean) {
		if (CryptoBean.class.isAssignableFrom(bean.getClass())) {
			CryptoBean cryptoBean = (CryptoBean) bean;
			cryptoBean.decrypt(secretKey, cryptorDelegater);
			return cryptoBean;
		} else if (bean instanceof String) {
			return cryptorDelegater.decrypt(secretKey, (String) bean);
		}
		return bean;
	}
}
