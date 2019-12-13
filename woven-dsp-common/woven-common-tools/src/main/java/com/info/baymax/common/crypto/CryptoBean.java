package com.info.baymax.common.crypto;

import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import org.apache.commons.lang3.StringUtils;

/**
 * 可加密解密对象接口
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午10:19:05
 */
public interface CryptoBean {

    /**
     * 加密处理
     */
    void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater);

    /**
     * 解密处理
     */
    void decrypt(CryptorDelegater cryptorDelegater);

    /**
     * 获取密文
     *
     * @param plaintext        明文信息
     * @param cryptorDelegater 加密解密器
     * @return 密文信息
     */
    default String ciphertext(String plaintext, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(plaintext)) {
            return cryptorDelegater.encrypt(cryptoType, plaintext);
        }
        return plaintext;
    }

    /**
     * 获取明文
     *
     * @param ciphertext       密文信息
     * @param cryptorDelegater 加密解密器
     * @return 明文信息
     */
    default String plaintext(String ciphertext, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(ciphertext)) {
            return cryptorDelegater.decrypt(ciphertext);
        }
        return ciphertext;
    }
}
