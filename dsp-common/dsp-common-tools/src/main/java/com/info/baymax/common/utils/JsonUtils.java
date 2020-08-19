package com.info.baymax.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Collection;
import java.util.Map;

public class JsonUtils {

	private static final ObjectMapper mapper = buildObjectMapper(false);

	private JsonUtils() {
	}

	public static String toJson(Object obj) {
		return toJson(obj, false);
	}

	public static String toJson(Object obj, boolean pretty) {
		try {
			if (pretty) {
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
			}
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, Class<T> typeClass) {
		if (json == null) {
			throw new IllegalArgumentException("json string should not be null");
		}
		try {
			return mapper.readValue(json, typeClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, JavaType valueType) {
		if (json == null) {
			throw new IllegalArgumentException("json string should not be null");
		}
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		if (json == null) {
			throw new IllegalArgumentException("json string should not be null");
		}
		try {
			return mapper.readValue(json, typeReference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <M extends Map<K, V>, K, V> M fromJson(String json, Class<M> mCkass, Class<K> kClass,
			Class<V> vClass) {
		return fromJson(json, contructMapType(mCkass, kClass, vClass));
	}

	public static <C extends Collection<O>, O> C fromJson(String json, Class<C> cClass, Class<O> oClass) {
		return fromJson(json, contructCollectionType(cClass, oClass));
	}

	@SuppressWarnings("rawtypes")
	public static JavaType contructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
	}

	@SuppressWarnings("rawtypes")
	public static JavaType contructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
		return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
	}

	public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
		return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	public static <T> T fromObject(Object obj, Class<T> typeClass) {
		return fromJson(toJson(obj), typeClass);
	}

	public static <T> T fromObject(Object obj, TypeReference<T> typeReference) {
		return fromJson(toJson(obj), typeReference);
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
}
