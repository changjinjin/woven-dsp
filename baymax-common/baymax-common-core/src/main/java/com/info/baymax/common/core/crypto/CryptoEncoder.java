package com.info.baymax.common.core.crypto;

import com.google.common.collect.Maps;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;

import java.util.Map;

/**
 * 数据加密自定义处理器
 *
 * @param <T> 加密数据类型
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public interface CryptoEncoder<T> {

    public static final Map<Class<? extends CryptoEncoder<?>>, CryptoEncoder<?>> cache = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    static <C extends CryptoEncoder<?>> C getInstance(Class<C> clazz) {
        CryptoEncoder<?> encoder = null;
        synchronized (cache) {
            encoder = cache.get(clazz);
            if (encoder == null) {
                try {
                    encoder = clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                cache.put(clazz, encoder);
            }
        }
        return (C) encoder;
    }

    /**
     * 自定制加密逻辑
     *
     * @param obj              需要加密的数据
     * @param secretKey        秘钥
     * @param wrapped          包裹选项
     * @param cryptoType       类型
     * @param cryptorDelegater 加解密委派器
     * @return 加密后的数据
     */
    T encode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater);

}
