package com.merce.woven.common.crypto.cryptors;

import java.util.Base64;

import com.merce.woven.common.crypto.CryptoType;
import com.merce.woven.common.crypto.annotation.MappedCryptoType;

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
    String decryptCiphertext(String ciphertext) {
        return new String(Base64.getDecoder().decode(ciphertext));
    }

    @Override
    String encryptPlaintext(String plaintext) {
        return Base64.getEncoder().encodeToString(plaintext.getBytes());
    }

}
