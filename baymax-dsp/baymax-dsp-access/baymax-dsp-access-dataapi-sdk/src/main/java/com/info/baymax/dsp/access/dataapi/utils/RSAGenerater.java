package com.info.baymax.dsp.access.dataapi.utils;

import lombok.Getter;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
public class RSAGenerater {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    public RSAGenerater() {
        generateKey();
    }

    /**
     * 生成公钥和私钥
     */
    private void generateKey() {
        // 1.初始化秘钥
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom sr = new SecureRandom(); // 随机数生成器
            keyPairGenerator.initialize(512, sr); // 设置512位长的秘钥
            KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 开始创建
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 进行转码
            publicKey = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
            // 进行转码
            privateKey = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 私钥匙加密或解密
     *
     * @param content
     * @param privateKeyStr
     * @return
     */
    public static String encryptByPrivateKey(String content, String privateKeyStr, int opmode) {
        // 私钥要用PKCS8进行处理
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
        KeyFactory keyFactory;
        PrivateKey privateKey;
        Cipher cipher;
        String text = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            // 还原Key对象
            privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(opmode, privateKey);
            // 加密解密操作
            text = encryptTxt(opmode, cipher, content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    public static String encryptByPrivateKey(String content, String privateKeyStr) {
        return encryptByPrivateKey(content, privateKeyStr, Cipher.ENCRYPT_MODE);
    }

    public static String decryptByPrivateKey(String content, String privateKeyStr) {
        return encryptByPrivateKey(content, privateKeyStr, Cipher.DECRYPT_MODE);
    }

    public static String encryptTxt(int opmode, Cipher cipher, String content) {
        byte[] result;
        String text = null;
        try {
            if (opmode == Cipher.ENCRYPT_MODE) { // 加密
                result = cipher.doFinal(content.getBytes());
                text = Base64.getEncoder().encodeToString(result);
            } else if (opmode == Cipher.DECRYPT_MODE) { // 解密
                result = cipher.doFinal(Base64.getDecoder().decode(content));
                text = new String(result, "UTF-8");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    /**
     * 公钥匙加密或解密
     *
     * @param content
     * @return
     */
    public static String encryptByPublicKey(String content, String publicKeyStr, int opmode) {
        // 公钥要用X509进行处理
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
        KeyFactory keyFactory;
        PublicKey publicKey;
        Cipher cipher;
        String text = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            // 还原Key对象
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(opmode, publicKey);
            // 加密解密操作
            text = encryptTxt(opmode, cipher, content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    public static String encryptByPublicKey(String content, String publicKeyStr) {
        return encryptByPublicKey(content, publicKeyStr, Cipher.ENCRYPT_MODE);
    }

    public static String decryptByPublicKey(String content, String publicKeyStr) {
        return encryptByPublicKey(content, publicKeyStr, Cipher.DECRYPT_MODE);
    }

    public static void main(String[] args) {

        // 1. 生成(公钥和私钥)密钥对
        RSAGenerater rsa = new RSAGenerater();
        System.out.println("公钥:" + rsa.publicKey);
        System.out.println("私钥:" + rsa.privateKey);
        System.out.println("私钥:" + rsa.privateKey.length());
        System.out.println("----------公钥加密私钥解密(推荐)，非对称加密，公钥保存在客户端，私钥保存在服务端-------------");
        // 使用 公钥加密,私钥解密
        String textsr = "irish";
        String encryptByPublic = RSAGenerater.encryptByPublicKey(textsr, rsa.publicKey, Cipher.ENCRYPT_MODE);
        System.out.println("公钥加密:" + encryptByPublic);
        String text = RSAGenerater.encryptByPrivateKey(encryptByPublic, rsa.privateKey, Cipher.DECRYPT_MODE);
        System.out.println("私钥解密:" + text);

        System.out.println("----------私钥加密公钥解密(不推荐),因为这样会把私钥暴露出来-------------");
        // 使用 私钥加密
        String body = "{\n" + "    \"loginName\":\"1+1\",\n" + "    \"password\":\"pass\"\n" + "}";
        String txtByPrivate = RSAGenerater.encryptByPrivateKey(body, rsa.privateKey, Cipher.ENCRYPT_MODE);
        System.out.println("私钥加密:" + txtByPrivate);

        String txtByPublic = RSAGenerater.encryptByPublicKey(txtByPrivate, rsa.publicKey, Cipher.DECRYPT_MODE);
        System.out.println("公钥解密:" + txtByPublic);
    }
}
