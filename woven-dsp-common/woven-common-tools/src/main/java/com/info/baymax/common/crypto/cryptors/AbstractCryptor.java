package com.info.baymax.common.crypto.cryptors;

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
    public String decrypt(String ciphertext) {
        if (ciphertext != null && isEncrypted(ciphertext)) {
            return decryptCiphertext(unwrapEncryptedValue(ciphertext));
        }
        return ciphertext;
    }

    @Override
    public String encrypt(String plaintext) {
        StringBuffer buff = new StringBuffer();
        if (plaintext != null && !isEncrypted(plaintext)) {
            return buff.append(prefix).append(encryptPlaintext(plaintext)).append(suffix).toString();
        }
        return plaintext;
    }

    /**
     * 解密没有标识包裹的密文
     *
     * @param unwrapEncryptedValue 没有包裹的密文本体
     * @return 明文
     */
    abstract String decryptCiphertext(String ciphertext);

    /**
     * 加密明文
     *
     * @param plaintext 明文
     * @return 密文，没有包裹前后缀
     */
    abstract String encryptPlaintext(String plaintext);

}
