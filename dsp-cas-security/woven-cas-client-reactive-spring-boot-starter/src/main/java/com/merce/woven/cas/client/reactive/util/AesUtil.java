package com.merce.woven.cas.client.reactive.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
public class AesUtil {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String enCode(final String content, final String key) {
        if (key == null || "".equals(key)) {
            log.warn("key\u4e3a\u7a7a\uff01");
            return null;
        }
        if (key.length() != 16) {
            log.error("key\u957f\u5ea6\u4e0d\u662f16\u4f4d\uff01");
            return null;
        }
        try {
            final byte[] raw = key.getBytes();
            final SecretKeySpec skey = new SecretKeySpec(raw, "AES");
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(1, skey);
            final byte[] byte_content = content.getBytes("utf-8");
            final byte[] encode_content = cipher.doFinal(byte_content);
            return Base64.encodeBase64String(encode_content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String deCode(final String content, final String key) {
        if (key == null || "".equals(key)) {
            log.warn("key\u4e3a\u7a7a\uff01");
            return null;
        }
        if (key.length() != 16) {
            log.error("key\u957f\u5ea6\u4e0d\u662f16\u4f4d\uff01");
            return null;
        }
        try {
            final byte[] raw = key.getBytes();
            final SecretKeySpec skey = new SecretKeySpec(raw, "AES");
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(2, skey);
            final byte[] encode_content = Base64.decodeBase64(content);
            final byte[] byte_content = cipher.doFinal(encode_content);
            return new String(byte_content, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
