package com.info.baymax.common.crypto.delegater;

import java.util.Collection;

import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.cryptors.Cryptor;

public interface CryptorDelegater {

    /**
     * 注册Cryptor对象
     *
     * @param cryptor
     */
    void register(Cryptor cryptor);

    /**
     * 注册Cryptor对象
     *
     * @param cryptors
     */
    void register(Cryptor... cryptors);

    /**
     * 注册Cryptor对象
     *
     * @param cryptors
     */
    void register(Collection<Cryptor> cryptors);

    /**
     * 获取可用的Cryptor对象
     *
     * @param ciphertext 密文
     * @return Cryptor对象
     */
    Cryptor get(String ciphertext);

    /**
     * 根据加密类型获取可用的Cryptor对象
     *
     * @param cryptoType 加密类型
     * @return Cryptor对象
     */
    Cryptor get(CryptoType cryptoType);

    /**
     * 加密
     *
     * @param plaintext  明文
     * @param secretKey  秘钥
     * @param wrapped    密文是否包裹
     * @param cryptoType 加密类型
     * @return 密文
     */
    String encrypt(String plaintext, String secretKey, boolean wrapped, CryptoType cryptoType);

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @param secretKey  秘钥
     * @param wrapped    密文是否包裹
     * @param cryptoType 加密类型
     * @return 明文
     */
    String decrypt(String ciphertext, String secretKey, boolean wrapped, CryptoType cryptoType);
}
