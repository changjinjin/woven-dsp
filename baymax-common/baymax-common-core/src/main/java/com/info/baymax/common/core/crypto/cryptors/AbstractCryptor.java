package com.info.baymax.common.core.crypto.cryptors;

import com.info.baymax.common.utils.Assert;

/**
 * Base64编码的字符串加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
public abstract class AbstractCryptor implements Cryptor {

    private String prefix;
    private String suffix;

    public AbstractCryptor() {
    }

    public AbstractCryptor(String prefix, String suffix) {
        Assert.notNull(prefix, "Prefix can't be null");
        Assert.notNull(suffix, "Suffix can't be null");
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public boolean isEncrypted(String ciphertext) {
        if (ciphertext == null) {
            return false;
        }
        final String trimmedValue = ciphertext.trim();
        return (trimmedValue.startsWith(prefix) && trimmedValue.endsWith(suffix));
    }

    @Override
    public boolean supports(String ciphertext) {
        return isEncrypted(ciphertext);
    }

    @Override
    public String unwrapEncryptedValue(String ciphertext) {
        return ciphertext.substring(prefix.length(), (ciphertext.length() - suffix.length()));
    }

    @Override
    public String decrypt(String ciphertext, String secretKey, boolean wrapped) {
        // 如果密文没有被包裹，直接解密
        if (!wrapped) {
            return decryptCiphertext(ciphertext, secretKey);
        }
        // 如果密文加密并且被包裹，需要拆包并且解密
        else if (ciphertext != null && isEncrypted(ciphertext)) {
            return decryptCiphertext(unwrapEncryptedValue(ciphertext), secretKey);
        }
        // 未加密，直接返回
        return ciphertext;
    }

    @Override
    public String encrypt(String plaintext, String secretKey, boolean wrapped) {
        StringBuffer buff = new StringBuffer();

        // 如果密码无需包裹则直接加密并返回
        if (!wrapped) {
            return encryptPlaintext(plaintext, secretKey);
        }
        // 明文没有加密并且需要包裹，这先加密并包裹返回
        else if (plaintext != null && !isEncrypted(plaintext)) {
            return buff.append(prefix).append(encryptPlaintext(plaintext, secretKey)).append(suffix).toString();
        }
        // 已经加密，直接返回
        return plaintext;
    }

    /**
     * 解密没有标识包裹的密文
     *
     * @param ciphertext 密文
     * @param secretKey  秘钥
     * @return 明文
     */
    abstract String decryptCiphertext(String ciphertext, String secretKey);

    /**
     * 加密明文
     *
     * @param plaintext 明文
     * @param secretKey 秘钥
     * @return 密文，没有包裹前后缀
     */
    abstract String encryptPlaintext(String plaintext, String secretKey);

}
