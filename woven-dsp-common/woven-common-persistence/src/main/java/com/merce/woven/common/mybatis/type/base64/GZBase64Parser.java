package com.merce.woven.common.mybatis.type.base64;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.merce.woven.common.utils.GZBase64Utils;

public interface GZBase64Parser {

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

}
