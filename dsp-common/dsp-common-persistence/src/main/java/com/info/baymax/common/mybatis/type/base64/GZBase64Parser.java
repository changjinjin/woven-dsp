package com.info.baymax.common.mybatis.type.base64;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.mybatis.type.JsonFormatTypeHandler;
import com.info.baymax.common.mybatis.type.TypeHandleException;
import com.info.baymax.common.utils.GZBase64Utils;

public interface GZBase64Parser extends JsonFormatTypeHandler {

    /**
     * 编码压缩
     *
     * @param s 编码压缩前的字符串
     * @return 编码压缩后的字符串
     * @throws IOException
     */
    default String encode(String s) throws IOException {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        return GZBase64Utils.compressString(s);
    }

    /**
     * 解压解码
     *
     * @param s 解压解码前的字符串
     * @return 解压解码后的字符串
     * @throws IOException
     */
    default String decode(String s) throws IOException {
        if (StringUtils.isNotEmpty(s) && !s.startsWith("[") && !s.startsWith("{") && !"null".equals(s)) {
            return GZBase64Utils.uncompressString(s);
        }
        return s;
    }

    default <T> String encodeToJson(T t) {
        try {
            return encode(toJson(t));
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <T> T decodeFromJson(String result, Class<T> tClass) {
        String decode = null;
        try {
            decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return fromJson(decode, tClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <T> T decodeFromJson(String result, TypeReference<T> typeReference) {
        String decode = null;
        try {
            decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return fromJson(decode, typeReference);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <C extends Collection<O>, O> C decodeFromJson(String result, Class<C> cClass, Class<O> oClass) {
        String decode = null;
        try {
            decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return fromJson(decode, cClass, oClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <M extends Map<K, V>, K, V> M decodeFromJson(String result, Class<M> mCkass, Class<K> kClass,
                                                         Class<V> vClass) {
        String decode = null;
        try {
            decode = decode(result);
            if (StringUtils.isEmpty(decode)) {
                return null;
            }
            return fromJson(decode, mCkass, kClass, vClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

}
