package com.info.baymax.common.core.crypto.method;

import com.info.baymax.common.core.crypto.*;
import com.info.baymax.common.core.crypto.annotation.Cryptoable;
import com.info.baymax.common.core.crypto.annotation.Decrypt;
import com.info.baymax.common.core.crypto.annotation.Encrypt;
import com.info.baymax.common.core.crypto.annotation.ReturnOperation;
import com.info.baymax.common.core.crypto.decoder.NoneCryptoDecoder;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.core.crypto.encoder.NoneCryptoEncoder;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;

public abstract class AbstractCryptoMethodInvoker implements CryptoMethodInvoker {

    /**
     * 全局的secretKey
     */
    private final String secretKey;

    /**
     * 加解密的处理器委派对象
     */
    private final CryptorDelegater cryptorDelegater;

    public AbstractCryptoMethodInvoker(String secretKey, CryptorDelegater cryptorDelegater) {
        this.secretKey = secretKey;
        this.cryptorDelegater = cryptorDelegater;
    }

    public Object[] handleArgs(Method method, Object[] args) {
        Cryptoable cryptoable = method.getAnnotation(Cryptoable.class);
        if (cryptoable == null || !cryptoable.enable()) {
            return args;
        }
        Parameter[] parameters = method.getParameters();
        if (cryptoable.enableParam() && parameters != null && parameters.length > 0) {
            Parameter parameter = null;
            Object obj = null;
            if (parameters != null && parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    parameter = parameters[i];
                    obj = args[i];

                    Encrypt encrypt = parameter.getAnnotation(Encrypt.class);
                    if (encrypt != null) {
                        obj = encryptObject(obj, encrypt.wrapped(), encrypt.cryptoType(), encrypt.encoder());
                    }
                    Decrypt decrypt = parameter.getAnnotation(Decrypt.class);
                    if (decrypt != null) {
                        obj = decryptObject(obj, decrypt.wrapped(), decrypt.cryptoType(), decrypt.decoder());
                    }

                    args[i] = obj;
                }
            }
        }
        return args;
    }

    public Object handleResult(Method method, Object result) {
        Cryptoable cryptoable = method.getAnnotation(Cryptoable.class);
        if (cryptoable == null || !cryptoable.enable()) {
            return result;
        }

        if (result != null) {
            // 如果指明了返回需要做处理才进行处理逻辑，否则跳过
            ReturnOperation[] returnOperations = cryptoable.returnOperation();
            if (returnOperations != null && returnOperations.length > 0) {

                // 处理前预处理
                Object obj = beforeHandleResult(result);

                // 开始加解密处理逻辑
                if (obj instanceof String) {
                    for (ReturnOperation returnOperation : returnOperations) {
                        obj = handleReturnResult(returnOperation, obj);
                    }
                    result = afterHandleResult(result, obj);
                } else {
                    for (ReturnOperation returnOperation : returnOperations) {
                        boolean flag = isCustom(returnOperation);
                        if (flag) {
                            result = handleReturnResult(returnOperation, obj);
                        } else {
                            handleReturnResult(returnOperation, obj);
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean isCustom(ReturnOperation returnOperation) {
        CryptoOperation cryptoOperation = returnOperation.cryptoOperation();
        boolean flag = false;
        switch (cryptoOperation) {
            case Decrypt:
                Class<? extends CryptoDecoder<?>> decoderClass = returnOperation.decoder();
                flag = (decoderClass != null && !NoneCryptoDecoder.class.isAssignableFrom(decoderClass));
                break;
            case Encrypt:
                Class<? extends CryptoEncoder<?>> encoderClass = returnOperation.encoder();
                flag = (encoderClass != null && !NoneCryptoEncoder.class.isAssignableFrom(encoderClass));
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    /**
     * 加解密处理完成后的组装结果
     *
     * @param result 原始返回值
     * @param obj    提取并处理后的对象
     * @return 组装后的返回值
     */
    public abstract Object afterHandleResult(Object result, Object obj);

    /**
     * 返回值处理前预处理，提取出内部的能够处理的CryptoBean对象，如CryptoBean的单个对象、集合、数组或字符串
     *
     * @param result 未提取的结果
     * @return 提取的对象
     */
    public abstract Object beforeHandleResult(Object result);

    /**
     * 处理方法返回值的加解密逻辑
     *
     * @param returnOperation 返回加解密操作描述
     * @param resultBody      报文对象
     */
    private Object handleReturnResult(ReturnOperation returnOperation, Object resultBody) {
        CryptoOperation cryptoOperation = returnOperation.cryptoOperation();
        switch (cryptoOperation) {
            case Encrypt:
                return encryptObject(resultBody, returnOperation.wrapped(), returnOperation.cryptoType(),
                    returnOperation.encoder());
            case Decrypt:
                return decryptObject(resultBody, returnOperation.wrapped(), returnOperation.cryptoType(),
                    returnOperation.decoder());
            default:
                return resultBody;
        }
    }

    // 处理较复杂对象的加密逻辑
    // FIXME 处理字符串数组和集合的不生效，需要把处理后的结果在包装到数组和集合中去，目前暂没有办法解决
    private Object encryptObject(Object obj, boolean wrapped, CryptoType cryptoType,
                                 Class<? extends CryptoEncoder<?>> encoderClass) {
        if (encoderClass != null && !NoneCryptoEncoder.class.isAssignableFrom(encoderClass)) {
            CryptoEncoder<?> encoder = CryptoEncoder.getInstance(encoderClass);
            return encoder.encode(obj, secretKey, wrapped, cryptoType, cryptorDelegater);
        } else {
            Class<? extends Object> clazz = obj.getClass();
            if (Iterable.class.isAssignableFrom(clazz)) {
                Iterator<?> iterator = ((Iterable<?>) obj).iterator();
                iterator.forEachRemaining(t -> {
                    // TODO 需要将处理的结果在放回到集合中去，因不知集合类型所以没法封装回原来的集合中去，建议先返回成string数组以支持改加密逻辑
                    encryptSingle(t, wrapped, cryptoType);
                });
            } else if (clazz.isArray()) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    encryptSingle(Array.get(obj, i), wrapped, cryptoType);
                }
            } else {
                return encryptSingle(obj, wrapped, cryptoType);
            }
            return obj;
        }
    }

    // 处理单个对象的解密逻辑
    private Object encryptSingle(Object bean, boolean wrapped, CryptoType cryptoType) {
        if (CryptoBean.class.isAssignableFrom(bean.getClass())) {
            CryptoBean cryptoBean = (CryptoBean) bean;
            cryptoBean.encrypt(secretKey, wrapped, cryptoType, cryptorDelegater);
            return cryptoBean;
        } else if (bean instanceof String) {
            return cryptorDelegater.encrypt((String) bean, secretKey, wrapped, cryptoType);
        }
        return bean;
    }

    // 处理较复杂对象的解密逻辑
    private Object decryptObject(Object obj, boolean wrapped, CryptoType cryptoType,
                                 Class<? extends CryptoDecoder<?>> decoderClass) {
        if (decoderClass != null && !NoneCryptoDecoder.class.isAssignableFrom(decoderClass)) {
            CryptoDecoder<?> decoder = CryptoDecoder.getInstance(decoderClass);
            return decoder.decode(obj, secretKey, wrapped, cryptoType, cryptorDelegater);
        } else {
            Class<? extends Object> clazz = obj.getClass();
            if (Iterable.class.isAssignableFrom(clazz)) {
                Iterator<?> iterator = ((Iterable<?>) obj).iterator();
                iterator.forEachRemaining(t -> {
                    // TODO 需要将处理的结果在放回到集合中去，因不知集合类型所以没法封装回原来的集合中去，建议先返回成string数组以支持改加密逻辑
                    decryptSingle(t, wrapped, cryptoType);
                });
            } else if (clazz.isArray()) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    decryptSingle(Array.get(obj, i), wrapped, cryptoType);
                }
            } else {
                return decryptSingle(obj, wrapped, cryptoType);
            }
            return obj;
        }
    }

    // 处理单个对象的加密逻辑
    private Object decryptSingle(Object bean, boolean wrapped, CryptoType cryptoType) {
        if (CryptoBean.class.isAssignableFrom(bean.getClass())) {
            CryptoBean cryptoBean = (CryptoBean) bean;
            cryptoBean.decrypt(secretKey, wrapped, cryptoType, cryptorDelegater);
            return cryptoBean;
        } else if (bean instanceof String) {
            return cryptorDelegater.decrypt((String) bean, secretKey, wrapped, cryptoType);
        }
        return bean;
    }
}
