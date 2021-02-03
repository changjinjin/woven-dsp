package com.info.baymax.common.core.crypto;

import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
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
     *
     * @param secretKey        秘钥
     * @param wrapped          密文是否需要包裹
     * @param cryptoType       加密类型
     * @param cryptorDelegater 加密解密器
     */
    void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater);

    /**
     * 解密处理
     *
     * @param secretKey        秘钥
     * @param wrapped          密文是否包裹了
     * @param cryptoType       加密类型
     * @param cryptorDelegater 加密解密器
     */
    void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater);

    /**
     * 获取密文
     *
     * @param plaintext        明文信息
     * @param secretKey        秘钥
     * @param wrapped          密文是否需要包裹
     * @param cryptoType       加密类型
     * @param cryptorDelegater 加密解密器
     * @return 密文信息
     */
    default String ciphertext(String plaintext, String secretKey, boolean wrapped, CryptoType cryptoType,
                              CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(plaintext)) {
            return cryptorDelegater.encrypt(plaintext, secretKey, wrapped, cryptoType);
        }
        return plaintext;
    }

    /**
     * 获取明文
     *
     * @param ciphertext       密文信息
     * @param secretKey        秘钥
     * @param wrapped          密文是否包裹了
     * @param cryptoType       加密类型
     * @param cryptorDelegater 加密解密器
     * @return 明文信息
     */
    default String plaintext(String ciphertext, String secretKey, boolean wrapped, CryptoType cryptoType,
                             CryptorDelegater cryptorDelegater) {
        if (StringUtils.isNotEmpty(ciphertext)) {
            return cryptorDelegater.decrypt(ciphertext, secretKey, wrapped, cryptoType);
        }
        return ciphertext;
    }
}
