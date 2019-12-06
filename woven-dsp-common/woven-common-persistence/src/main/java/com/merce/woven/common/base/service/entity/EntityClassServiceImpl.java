package com.merce.woven.common.base.service.entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EntityClassServiceImpl<T> implements EntityClassService<T> {

	protected Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public EntityClassServiceImpl() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		entityClass = (Class<T>) params[0];
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

}
