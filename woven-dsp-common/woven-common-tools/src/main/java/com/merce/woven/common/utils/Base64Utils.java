package com.merce.woven.common.utils;

import java.util.Base64;
import java.util.regex.Pattern;

public class Base64Utils {

    public static boolean check(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    public static String encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String decode(String src) {
        return new String(Base64.getDecoder().decode(src));
    }

    public static void main(String[] args) {
        String str = "weuyeriuiuer==";
        System.out.println(check(str));
        String encode = encode(str);
        System.out.println(encode);
        System.out.println(check(encode));
        System.out.println(decode(encode));
    }
}
