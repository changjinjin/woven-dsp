package com.info.baymax.common.queryapi.record;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.google.common.collect.Sets;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.queryapi.field.Sort;
import com.info.baymax.common.queryapi.page.IPageable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AbstractQuery 构建常用的条件结构
 *
 * @param <T> AbstractQuery子类
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:13:02
 */
@ApiModel
@Setter
@Getter
@ToString
@SuppressWarnings("unchecked")
public abstract class AbstractPropertiesQuery<T extends AbstractPropertiesQuery<T>>
		extends AbstractQuery<AbstractPropertiesQuery<T>> implements PropertiesQueryBuilder<T> {
	private static final long serialVersionUID = 4850854513242762929L;

	@ApiModelProperty("查询的字段列表")
	protected LinkedHashSet<String> selectProperties;

	@ApiModelProperty("排除的字段列表")
	protected LinkedHashSet<String> excludeProperties;

	public AbstractPropertiesQuery() {
		super();
	}

	@Override
	public T selectProperties(Collection<String> selectProperties) {
		if (selectProperties != null && !selectProperties.isEmpty()) {
			if (this.selectProperties == null) {
				this.selectProperties = Sets.newLinkedHashSet();
			}
			this.selectProperties.addAll(selectProperties);
		}
		return (T) this;
	}

	@Override
	public T excludeProperties(Collection<String> excludeProperties) {
		if (excludeProperties != null && !excludeProperties.isEmpty()) {
			if (this.excludeProperties == null) {
				this.excludeProperties = Sets.newLinkedHashSet();
			}
			this.excludeProperties.addAll(excludeProperties);
		}
		return (T) this;
	}

	@Override
	public T clearSelectProperties() {
		clear(this.selectProperties);
		return (T) this;
	}

	@Override
	public T clearExcludeProperties() {
		clear(this.excludeProperties);
		return (T) this;
	}

	@Override
	public T clear() {
		super.clear();
		clearSelectProperties();
		clearExcludeProperties();
		return (T) this;
	}

	@Override
	public T fieldGroup(FieldGroup fieldGroup) {
		super.fieldGroup(fieldGroup);
		return (T) this;
	}

	@Override
	public T sort(Sort sort) {
		super.sort(sort);
		return (T) this;
	}

	@Override
	public T orderBy(String fieldName) {
		super.orderBy(fieldName);
		return (T) this;
	}

	@Override
	public T orderByAsc(String fieldName) {
		super.orderByAsc(fieldName);
		return (T) this;
	}

	@Override
	public T orderByDesc(String fieldName) {
		super.orderByDesc(fieldName);
		return (T) this;
	}

	@Override
	public T sorts(Collection<Sort> sorts) {
		super.sorts(sorts);
		return (T) this;
	}

	@Override
	public T sorts(Sort... sorts) {
		super.sorts(sorts);
		return (T) this;
	}

	@Override
	public T clearFieldGroup() {
		super.clearFieldGroup();
		return (T) this;
	}

	@Override
	public T clearSorts() {
		super.clearSorts();
		return (T) this;
	}

	@Override
	public T paged() {
		super.paged();
		return (T) this;
	}

	@Override
	public T unpaged() {
		super.unpaged();
		return (T) this;
	}

	@Override
	public T page(int pageNum, int pageSize) {
		super.page(pageNum, pageSize);
		return (T) this;
	}

	@Override
	public T offset(int offset, int limit) {
		super.offset(offset, limit);
		return (T) this;
	}

	@Override
	public T orderBy(String fieldName, boolean asc) {
		super.orderBy(fieldName, asc);
		return (T) this;
	}

	@Override
	public T orderByAsc(String... fieldNames) {
		super.orderByAsc(fieldNames);
		return (T) this;
	}

	@Override
	public T orderByDesc(String... fieldNames) {
		super.orderByDesc(fieldNames);
		return (T) this;
	}

	@Override
	public T paged(boolean pageable) {
		super.paged(pageable);
		return (T) this;
	}

	@Override
	public T pageable(IPageable pageable) {
		super.pageable(pageable);
		return (T) this;
	}

	@Override
	public T pageNum(int pageNum) {
		super.pageNum(pageNum);
		return (T) this;
	}

	@Override
	public T pageSize(int pageSize) {
		super.pageSize(pageSize);
		return (T) this;
	}

	@Override
	public T clearPageable() {
		super.clearPageable();
		return (T) this;
	}

}
