package com.info.baymax.common.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

    private static final ObjectMapper mapper = buildObjectMapper(false);

    private JsonUtils() {
    }

    public static ObjectMapper buildObjectMapper(boolean prettyJson) {
        ObjectMapper m = new ObjectMapper();
        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        m.setSerializationInclusion(Include.NON_NULL);
        m.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        return m;
    }

    @SuppressWarnings("rawtypes")
    public static <C extends Collection, V> JavaType contructCollectionType(Class<C> collectionClass,
                                                                            Class<V> elementClass) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    @SuppressWarnings("rawtypes")
    public static <M extends Map, K, V> JavaType contructMapType(Class<M> mapClass, Class<K> keyClass,
                                                                 Class<V> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static <M extends Map<K, V>, K, V> M fromJson(String src, Class<M> mCkass, Class<K> kClass,
                                                         Class<V> vClass) {
        return fromJson(src, contructMapType(mCkass, kClass, vClass));
    }

    public static <C extends Collection<O>, O> C fromJson(String src, Class<C> cClass, Class<O> oClass) {
        return fromJson(src, contructCollectionType(cClass, oClass));
    }

    public static <T> T fromJson(String src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(URL src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(URL src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(URL src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(File src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(File src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(File src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(Reader src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(Reader src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(Reader src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(InputStream src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, int offset, int len, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, int offset, int len, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(byte[] src, int offset, int len, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, offset, len, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(DataInput src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(DataInput src, JavaType valueType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, Class<T> typeClass) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, typeClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, TypeReference<T> typeReference) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromObject(Object src, JavaType javaType) {
        Assert.notNull(src, "src should not be null");
        try {
            return mapper.convertValue(src, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(File dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(OutputStream dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(DataOutput dest, Object value) {
        Assert.notNull(dest, "dest should not be null");
        try {
            mapper.writeValue(dest, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJson(Object value) {
        return toJson(value, false);
    }

    public static String toJson(Object value, boolean pretty) {
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toByte(Object value) {
        try {
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsBytes(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
