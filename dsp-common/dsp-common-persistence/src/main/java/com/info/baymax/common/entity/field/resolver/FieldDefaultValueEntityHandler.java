package com.info.baymax.common.entity.field.resolver;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.entity.field.convertor.UnknownValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertor;
import com.info.baymax.common.entity.field.convertor.ValueConvertorRegistry;
import com.info.baymax.common.entity.handle.EntityHandler;
import com.info.baymax.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的属性值初始化处理器
 *
 * @author jingwei.yang
 * @date 2020年3月19日 上午9:58:48
 */
@Slf4j
public class FieldDefaultValueEntityHandler implements EntityHandler {

	/**
	 * 缓存已经解析的实体类信息，减少消耗
	 */
	private static final Set<Class<? extends Object>> entityClassCache = new HashSet<>();

	/**
	 * 缓存已经解析过并且存在需要处理字段的实体类和对应字段，目的是只处理需要处理的字段，跳过不需要处理的字段，减少计算量
	 */
	private static final Map<Class<? extends Object>, List<EntityField>> annotationedEntityFieldsCache = new HashMap<>();

	/**
	 * 单实例的FieldResolver对象
	 */
	private static EntityHandler singleton = null;

	/**
	 * 双检锁实现单利模式，避免频繁创建对象的消耗和线程安全问题
	 */
	public static EntityHandler getInstance() {
		if (singleton == null) {
			synchronized (FieldDefaultValueEntityHandler.class) {
				if (singleton == null) {
					singleton = new FieldDefaultValueEntityHandler();
				}
			}
		}
		return singleton;
	}

	// 只允许内部调用
	private FieldDefaultValueEntityHandler() {
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public boolean supports(Object t) {
		return true;
	}

	@Override
	public void handle(Object obj) {
		Class<? extends Object> entityClass = obj.getClass();
		if (entityClassCache.contains(entityClass) && !annotationedEntityFieldsCache.containsKey(entityClass)) {
			log.debug("Class {} has no field with annotation @DefaultValue, skiped!", entityClass.getName());
			return;
		} else if (entityClassCache.contains(entityClass) && annotationedEntityFieldsCache.containsKey(entityClass)) {
			processFields(obj, annotationedEntityFieldsCache.get(entityClass));
		} else {
			List<EntityField> fields = FieldHelper.getFields(entityClass);
			entityClassCache.add(entityClass);

			if (ICollections.hasNoElements(fields)) {
				log.debug("Class {} has no one field, skiped!", entityClass.getName());
				return;
			}
			List<EntityField> cachedFields = fields.stream()
					.filter(field -> field.isAnnotationPresent(DefaultValue.class)).collect(Collectors.toList());
			if (ICollections.hasElements(cachedFields)) {
				annotationedEntityFieldsCache.put(obj.getClass(), cachedFields);
				processFields(obj, cachedFields);
			}
		}
	}

	// 处理field
	private void processFields(Object obj, List<EntityField> cachedFields) {
		try {
			for (EntityField field : cachedFields) {
				processField(obj, field);
			}
		} catch (Exception e) {
			throw new FieldResolveException(e.getMessage(), e);
		}
	}

	// 处理field
	private void processField(Object object, EntityField field)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (field.isAnnotationPresent(DefaultValue.class)) {
			processDefaultValue(object, field, field.getAnnotation(DefaultValue.class));
		}
	}

	// 设置field默认值
	private void processDefaultValue(Object object, EntityField field, DefaultValue defaultValue)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object value = field.getValue(object);
		if (value == null) {
			ValueConvertor<?> valueConvertor = null;
			ValueConvertorRegistry registry = ValueConvertorRegistry.getInstance();

			String defValue = defaultValue.value();
			Class<? extends ValueConvertor<?>> convertorClass = defaultValue.convertor();
			if (convertorClass == null || convertorClass.isAssignableFrom(UnknownValueConvertor.class)) {
				valueConvertor = registry.getValueConvertor(field.getJavaType());
			} else {
				valueConvertor = registry.getConvertor(null, convertorClass);
			}
			field.setValue(object, valueConvertor.convert(defValue));
		}
	}

}
