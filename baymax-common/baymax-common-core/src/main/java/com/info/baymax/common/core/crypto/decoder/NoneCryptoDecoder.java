package com.info.baymax.common.core.crypto.decoder;

import com.info.baymax.common.core.crypto.CryptoDecoder;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;

/**
 * 默认的无处理的数据解密器
 *
 * @param <T> 解密数据类型
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public class NoneCryptoDecoder implements CryptoDecoder<Object> {

    @Override
    public Object decode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        return obj;
    }

}
