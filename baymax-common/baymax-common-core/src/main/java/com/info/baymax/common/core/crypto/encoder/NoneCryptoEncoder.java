package com.info.baymax.common.core.crypto.encoder;

import com.info.baymax.common.core.crypto.CryptoEncoder;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;

/**
 * 默认的无处理的数据加密器
 *
 * @param <T> 数据类型
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public class NoneCryptoEncoder implements CryptoEncoder<Object> {

    @Override
    public Object encode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType,
                         CryptorDelegater cryptorDelegater) {
        return obj;
    }

}
