package com.info.baymax.dsp.access.dataapi.utils;

import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.common.utils.crypto.AESUtil;

public class EncryptUtils {

    public static Object encrypt(Object obj, boolean encrypted, String secretKey) {
        if (encrypted) {
            return AESUtil.encrypt(serialize(obj), secretKey);
        }
        return obj;
    }

    public static String serialize(Object obj) {
        return JsonUtils.toJson(obj);
    }
}
