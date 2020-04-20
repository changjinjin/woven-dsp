package com.info.baymax.common.crypto.delegater;

import com.info.baymax.common.crypto.CryptoException;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.MappedCryptoType;
import com.info.baymax.common.crypto.cryptors.AesCryptor;
import com.info.baymax.common.crypto.cryptors.Base64Cryptor;
import com.info.baymax.common.crypto.cryptors.Cryptor;
import com.info.baymax.common.crypto.cryptors.DesCryptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密解密器委派处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 下午12:29:18
 */
public class DefaultCryptorDelegater implements CryptorDelegater/* , ApplicationContextAware */ {

	private Map<CryptoType, Cryptor> cryptors = new HashMap<>();

	public DefaultCryptorDelegater() {
		register(new Base64Cryptor(), new AesCryptor(), new DesCryptor());
	}

	public DefaultCryptorDelegater(Map<CryptoType, Cryptor> cryptors) {
		this.cryptors = cryptors;
	}

	/*
	 * @Override public void setApplicationContext(ApplicationContext applicationContext) {
	 * initStrategies(applicationContext); }
	 * 
	 * protected void initStrategies(ApplicationContext context) { Map<String, Cryptor> beans =
	 * BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Cryptor.class, true, false); if (beans != null &&
	 * !beans.isEmpty()) { Collection<Cryptor> values = beans.values(); for (Cryptor cryptor : values) {
	 * register(cryptor); } } }
	 */

	@Override
	public String decrypt(String ciphertext, String secretKey) {
		Cryptor cryptor = get(ciphertext);
		if (cryptor != null) {
			return cryptor.decrypt(ciphertext, secretKey);
		}
		return ciphertext;
	}

	@Override
	public void register(Cryptor cryptor) {
		// 必须通过注解指定映射的加解密类型
		MappedCryptoType mappedCryptoType = cryptor.getClass().getAnnotation(MappedCryptoType.class);
		if (mappedCryptoType == null) {
			throw new CryptoException(String.format(
					"Unmapped Cryptor %s, Please specify the mapping type use Annotation '@MappedCryptoType'.",
					cryptor.getClass().getName()));
		}
		// 查询是否已经注册过该实例，同类型的只能注册一个
		Cryptor existCryptor = cryptors.get(mappedCryptoType.value());
		if (existCryptor != null) {
			throw new CryptoException(String.format("More than one Cryptor Mapped to %s, they are %s and %s",
					mappedCryptoType, cryptor.getClass().getName(), existCryptor.getClass().getName()));
		}
		cryptors.put(mappedCryptoType.value(), cryptor);
	}

	@Override
	public void register(Cryptor... cryptors) {
		for (Cryptor cryptor : cryptors) {
			register(cryptor);
		}
	}

	@Override
	public void register(Collection<Cryptor> cryptors) {
		for (Cryptor cryptor : cryptors) {
			register(cryptor);
		}
	}

	@Override
	public Cryptor get(CryptoType cryptoType) {
		if (!cryptors.isEmpty()) {
			return cryptors.get(cryptoType);
		}
		return null;
	}

	@Override
	public String encrypt(String plaintext, String secretKey, CryptoType cryptoType) {
		if (cryptoType == null || plaintext == null) {
			throw new IllegalArgumentException("CryptoType can't be null.");
		}
		Cryptor cryptor = get(cryptoType);
		if (cryptor == null) {
			throw new CryptoException(String.format("No suitable Cryptor to use for cryptoType:%s", cryptoType));
		}
		return cryptor.encrypt(plaintext, secretKey);
	}

	@Override
	public Cryptor get(String ciphertext) {
		if (!cryptors.isEmpty()) {
			Collection<Cryptor> values = cryptors.values();
			for (Cryptor cryptor : values) {
				if (cryptor.supports(ciphertext)) {
					return cryptor;
				}
			}
		}
		return null;
	}

}
