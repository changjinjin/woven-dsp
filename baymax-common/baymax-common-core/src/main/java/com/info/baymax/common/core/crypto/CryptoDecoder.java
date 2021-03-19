package com.info.baymax.common.core.crypto;

import com.google.common.collect.Maps;
import com.info.baymax.common.core.crypto.decoder.NoneCryptoDecoder;
import com.info.baymax.common.core.crypto.decoder.StringListCryptoDecoder;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;

import java.util.Map;

/**
 * 数据解密自定义处理器
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2021年3月10日 下午4:59:28
 */
public interface CryptoDecoder<T> {

    public static final Map<Class<? extends CryptoDecoder<?>>, CryptoDecoder<?>> cache = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    static <C extends CryptoDecoder<?>> C getInstance(Class<C> clazz) {
        CryptoDecoder<?> decoder = null;
        synchronized (cache) {
            decoder = cache.get(clazz);
            if (decoder == null) {
                try {
                    decoder = clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                cache.put(clazz, decoder);
            }
        }
        return (C) decoder;
    }

    /**
     * 自定制解密逻辑
     *
     * @param obj              需要解密的数据
     * @param secretKey        秘钥
     * @param wrapped          包裹选项
     * @param cryptoType       类型
     * @param cryptorDelegater 加解密委派器
     * @return 解密后的数据
     */
    T decode(Object obj, String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater);

    public static void main(String[] args) {
        StringListCryptoDecoder instance = CryptoDecoder.getInstance(StringListCryptoDecoder.class);
        System.out.println(NoneCryptoDecoder.class.isAssignableFrom(instance.getClass()));
        System.out.println(StringListCryptoDecoder.class.isAssignableFrom(instance.getClass()));
    }
}
