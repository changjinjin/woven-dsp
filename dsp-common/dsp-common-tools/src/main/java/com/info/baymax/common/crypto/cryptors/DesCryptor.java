package com.info.baymax.common.crypto.cryptors;

import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.annotation.MappedCryptoType;
import com.info.baymax.common.utils.crypto.DESUtil;

/**
 * AES加密的字符串加密解密处理器
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@MappedCryptoType(CryptoType.DES)
public class DesCryptor extends AbstractCryptor {

    public DesCryptor() {
        super("DES(", ")");
    }

    @Override
    String decryptCiphertext(String ciphertext, String secretKey) {
        return DESUtil.decrypt(ciphertext, secretKey);
    }

    @Override
    String encryptPlaintext(String plaintext, String secretKey) {
        return DESUtil.encrypt(plaintext, secretKey);
    }

}
