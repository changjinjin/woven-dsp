package com.info.baymax.dsp.access.dataapi.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    // 默认字符集
    private static final String DEFAULT_CARSET = "UTF-8";
    // 加密方式
    private static final String AES = "AES";
    // 使用的加密算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    // 加密结果转换成多少进制的字符串
    private static final int RADIX = 16;

    /**
     * aes解密
     *
     * @param encrypt 内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypt, String key) {
        try {
            return aesDecrypt(encrypt, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * aes加密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key) {
        try {
            return aesEncrypt(content, key);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), AES));

        return cipher.doFinal(content.getBytes(DEFAULT_CARSET));
    }

    /**
     * AES加密为十六进制
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    private static String aesEncrypt(String content, String encryptKey) throws Exception {
        byte[] encodArroy = aesEncryptToBytes(content, encryptKey);
        return bytesToHex(encodArroy);
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), AES));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 解密字符串
     *
     * @param encryptStr 需要解密的字符串
     * @param decryptKey 解密秘钥
     * @return
     * @throws Exception
     */
    private static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return (encryptStr != null && encryptStr.length() > 0)
            ? aesDecryptByBytes(stringToByte(encryptStr, RADIX), decryptKey)
            : null;
    }

    // Byte[] ->十六进制
    private static String bytesToHex(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将指定进制的字符串转换成byte数组
     *
     * @param hexStr
     * @param radix  指定是多少进制，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return
     */
    private static byte[] stringToByte(String hexStr, int radix) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), radix);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), radix);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    // TEST
    public static void main(String[] args) throws Exception {
        // 密钥 (需要前端和后端保持一致)
        String KEY = "infoaeskey123456";
        String content = "admin";
        System.out.println("加密前：" + content);
        System.out.println("加密密钥和解密密钥：" + KEY);
        String encrypt = encrypt(content, KEY);
        encrypt = "0be8361ca73b1598caaf81b1f542116a";
        encrypt = "6bcf7bc2ab6b56ae7bcd80037f6852e0";
        System.out.println("加密后：" + encrypt);
        String decrypt = decrypt(encrypt, KEY);
        System.out.println("解密后：" + decrypt);
    }
}
