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
    void encrypt(String secretKey, CryptoType cryptoType, CryptorDelegater cryptorDelegater);

    /**
     * 解密处理
     */
    void decrypt(String secretKey, CryptorDelegater cryptorDelegater);

    /**
     * 获取密文
     *
     * @param plaintext        明文信息
     * @param secretKey        秘钥
     * @param cryptoType       加密类型
     * @param cryptorDelegater 加密解密器
     * @return 密文信息
     */
    default String ciphertext(String plaintext, String secretKey, CryptoType cryptoType,
                              CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(plaintext)) {
            return cryptorDelegater.encrypt(plaintext, secretKey, cryptoType);
        }
        return plaintext;
    }

    /**
     * 获取明文
     *
     * @param ciphertext       密文信息
     * @param secretKey        秘钥
     * @param cryptorDelegater 加密解密器
     * @return 明文信息
     */
    default String plaintext(String ciphertext, String secretKey, CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(ciphertext)) {
            return cryptorDelegater.decrypt(ciphertext, secretKey);
        }
        return ciphertext;
    }
}
