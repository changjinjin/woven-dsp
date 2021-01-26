package com.info.baymax.common;

import org.junit.Test;

import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.crypto.delegater.DefaultCryptorDelegater;

public class CryptoTest {

	/**
	 * 加解密处理器委派器
	 */
	private CryptorDelegater cryptorDelegater = new DefaultCryptorDelegater();

	/**
	 * 公共秘钥
	 */
	private static final String secretKey = "infoaeskey123456";

	/**
	 * 测试明文，比如：密码 "123456"
	 */
	private String plaintext = "123456";

	@Test
	public void test() {
		// AES
		crypto(CryptoType.AES, true, plaintext, secretKey);
		crypto(CryptoType.AES, false, plaintext, secretKey);

		// BASE64
		crypto(CryptoType.BASE64, true, plaintext, secretKey);
		crypto(CryptoType.BASE64, false, plaintext, secretKey);

		// DES
		crypto(CryptoType.DES, true, plaintext, secretKey);
		crypto(CryptoType.DES, false, plaintext, secretKey);
	}

	/**
	 * 加解密测试方法
	 * 
	 * @param cryptoType 加密类型，如：AES,BASE64
	 * @param wrapped    密文是否使用算法标识包裹
	 * @param plaintext  明文
	 * @param secretKey  秘钥
	 */
	public void crypto(CryptoType cryptoType, boolean wrapped, String plaintext, String secretKey) {
		System.out.println("====>CryptoType = " + cryptoType + ", wrapped = " + wrapped + ", plaintext = " + plaintext
				+ ", secretKey = " + secretKey);

		// 加密
		String ciphertext = cryptorDelegater.encrypt(plaintext, secretKey, wrapped, cryptoType);
		System.out.println("===> 加密后密文: " + ciphertext);

		// 解密
		plaintext = cryptorDelegater.decrypt(ciphertext, secretKey, wrapped, cryptoType);
		System.out.println("===> 解密后明文: " + plaintext);
		System.out.println("\n\n");
	}

}
