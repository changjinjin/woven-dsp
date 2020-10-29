package com.info.baymax.common.crypto.cryptors;

/**
 * 加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
public interface Cryptor {

    /**
     * 是否是加密的密文
     *
     * @param ciphertext 密文信息，可能包含加密标记等信息，如：ENC(abc)
     * @return 是否是加密的密文
     */
    boolean isEncrypted(String ciphertext);

    /**
     * 是否适配
     *
     * @param ciphertext 密文
     * @return 是否适配
     */
    boolean supports(String ciphertext);

    /**
     * 提取密文的文本信息，即抽取出密文的前缀或者后缀，返回密文本体，如：ENC(abc) 返回 abc
     *
     * @param ciphertext 带有标记的密文
     * @return 密文本体
     */
    String unwrapEncryptedValue(String ciphertext);

    /**
     * 加密
     *
     * @param plaintext 明文
     * @param secretKey 秘钥
     * @param wrapped   是否需要包裹密文
     * @return 密文
     */
    String encrypt(String plaintext, String secretKey, boolean wrapped);

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @param secretKey  秘钥
     * @param wrapped    密文是否是包裹的
     * @return 明文
     */
    String decrypt(String ciphertext, String secretKey, boolean wrapped);

}
