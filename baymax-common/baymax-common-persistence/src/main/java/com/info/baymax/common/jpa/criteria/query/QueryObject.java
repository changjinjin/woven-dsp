package com.info.baymax.common.jpa.criteria.query;

import com.google.common.collect.Sets;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.ComparatorOperator;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.OrderDirection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询参数包装类
 *
 * @author jingwei.yang
 * @date 2019年5月5日 下午12:08:57
 */
@Getter
@Setter
@ApiModel
public class QueryObject implements Serializable {
	private static final long serialVersionUID = -6814906254023484410L;

	public static final int DEFAULT_OFFSET = 0;
	public static final int DEFAULT_LIMIT = 8;
	public static final int MAX_LIMIT = Integer.MAX_VALUE;

	/**
	 * 查询条件属性列表
	 */
	@ApiModelProperty("查询条件属性列表")
	private List<FieldObject> fieldList;

	/**
	 * 排序属性信息
	 */
	@ApiModelProperty("排序属性信息")
	private SortObject sortObject;

	/**
	 * 分组属性信息
	 */
	@ApiModelProperty("分组属性信息")
	private JpaCriteriaHelper.GroupBy groupBy;

	/**
	 * 偏移量
	 */
	@ApiModelProperty("偏移量")
	private Integer offset;

	/**
	 * 页长
	 */
	@ApiModelProperty("页长")
	private Integer limit;

	private QueryObject() {
	}

	private QueryObject(Integer offset, Integer limit) {
		this.offset = offset;
		this.limit = limit;
	}

	public List<FieldObject> getFieldList() {
		formatFields();
		return fieldList;
	}

	public Integer getPageNum() {
		if (offset != null && limit != null) {
			return (int) Math.floor((offset + limit) / limit);
		}
		return 1;
	}

	// ################################Builder################################//

	public static QueryObject builder() {
		return new QueryObject(DEFAULT_OFFSET, DEFAULT_LIMIT);
	}

	public static QueryObject builder(QueryObject queryObject) {
		return queryObject == null ? builder() : queryObject;
	}

	public static QueryObject builder(int offset, int limit) {
		return builder().offset(offset).limit(limit);
	}

	public QueryObject uppaged() {
		return offset(DEFAULT_OFFSET).limit(MAX_LIMIT);
	}

	public QueryObject page(int pageNum, int pageSize) {
		return offset((pageNum - 1) * pageSize).limit(pageSize);
	}

	public QueryObject offset(int offset) {
		setOffset(offset);
		return this;
	}

	public QueryObject limit(int limit) {
		setLimit(limit);
		return this;
	}

	public QueryObject addFields(List<FieldObject> fieldObjects) {
		if (fieldList == null) {
			fieldList = new ArrayList<FieldObject>();
		}
		if (fieldObjects != null && fieldObjects.size() > 0) {
			this.fieldList.addAll(fieldObjects);
		}
		return this;
	}

	public QueryObject addField(String fieldName, Object fieldValue) {
		addField(new FieldObject(fieldName, fieldValue, ComparatorOperator.EQUAL));
		return this;
	}

	public QueryObject addField(String fieldName, Object fieldValue,
			JpaCriteriaHelper.ComparatorOperator comparatorOperator) {
		addField(new FieldObject(fieldName, fieldValue, comparatorOperator));
		return this;
	}

	public QueryObject addField(String fieldName, Object fieldValue,
			JpaCriteriaHelper.ComparatorOperator comparatorOperator,
			JpaCriteriaHelper.LogicalOperator logicalOperator) {
		addField(new FieldObject(fieldName, fieldValue, comparatorOperator, logicalOperator));
		return this;
	}

	public QueryObject addField(FieldObject fieldObject) {
		if (fieldList == null) {
			fieldList = new ArrayList<FieldObject>();
		}
		if (fieldObject != null) {
			this.fieldList.add(fieldObject);
		}
		return this;
	}

	public QueryObject removeField(FieldObject fieldObject) {
		if (fieldList != null) {
			fieldList.remove(fieldObject);
		}
		return this;
	}
	public QueryObject removeFields(FieldObject... fieldObjects) {
		if (fieldList != null) {
			fieldList.removeAll(Sets.newHashSet(fieldObjects));
		}
		return this;
	}
	
	public QueryObject removeField(String fieldName) {
		return removeFields(fieldName);
	}
	
	public QueryObject removeFields(String... fieldNames) {
		if (fieldList != null) {
			HashSet<String> set = Sets.newHashSet(fieldNames);
			Iterator<FieldObject> iterator = fieldList.iterator();
			while (iterator.hasNext()) {
				FieldObject next = iterator.next();
				if (set.contains(next.getFieldName())) {
					iterator.remove();
				}
			}
		}
		return this;
	}

	public QueryObject orderBy(String field, OrderDirection orderDirection) {
		orderBy(new SortObject(field, orderDirection));
		return this;
	}

	public QueryObject orderBy(String field) {
		if (StringUtils.isNotEmpty(field)) {
			return orderBy(field, OrderDirection.ASC);
		}
		return this;
	}

	public QueryObject orderByDesc(String field) {
		if (StringUtils.isNotEmpty(field)) {
			return orderBy(field, OrderDirection.DESC);
		}
		return this;
	}

	private QueryObject orderBy(SortObject sortObject) {
		setSortObject(sortObject);
		return this;
	}

	public QueryObject groupBy(String fieldName) {
		groupBy(new JpaCriteriaHelper.GroupBy(fieldName));
		return this;
	}

	public QueryObject groupBy(JpaCriteriaHelper.GroupBy groupBy) {
		setGroupBy(groupBy);
		return this;
	}

	public QueryObject clearFieldList() {
		if (fieldList != null) {
			fieldList.clear();
		}
		return this;
	}

	public QueryObject clearOffset() {
		setOffset(DEFAULT_OFFSET);
		return this;
	}

	public QueryObject clearLimit() {
		setLimit(DEFAULT_LIMIT);
		return this;
	}

	public QueryObject clearOrderBy() {
		setSortObject(null);
		return this;
	}

	public QueryObject clearGroupBy() {
		setGroupBy(null);
		return this;
	}

	public QueryObject clear() {
		clearFieldList();
		clearOffset();
		clearLimit();
		clearOrderBy();
		clearGroupBy();
		return this;
	}

	public QueryObject formatFields() {
		if (fieldList != null)
			for (FieldObject field : fieldList) {
				String fieldName = field.getFieldName();
				Object fieldValue = field.getFieldValue();
				if ("lastModifiedTime".equals(fieldName) || "createTime".equals(fieldName)) {
					if (fieldValue instanceof String) {
						String value = fieldValue.toString();
						if (value.contains("TO")) {
							value = value.substring(1, value.length() - 1);
							String[] range = value.split(" TO ");
							range[0] = (range[0].replace("Z", " UTC"));
							range[1] = (range[1].replace("Z", " UTC"));
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
							Date start = null;
							Date end = null;
							try {
								start = format.parse(range[0]);
								end = format.parse(range[1]);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							Object[] objects = new Object[2];
							objects[0] = start;
							objects[1] = end;
							field.setFieldValue(objects);
						}
					} else if (fieldValue instanceof Long) {
						field.setFieldValue(new Date((long) fieldValue));
					}
				}
			}
		return this;
	}

	public QueryObject setCurrentTenantCondition(Map<String, Object> conditions) {
		String tenantId = conditions.getOrDefault("tenantId", "").toString();
		if (StringUtils.isNotEmpty(tenantId)) {
			addField("tenantId", tenantId);
		}
		return this;
	}

	public QueryObject setCurrentUserCondition(Map<String, Object> conditions) {
		boolean admin = Boolean.valueOf(conditions.getOrDefault("admin", false).toString());
		String tenantId = conditions.getOrDefault("tenantId", "").toString();
		String owner = conditions.getOrDefault("owner", "").toString();

		if (StringUtils.isNotEmpty(tenantId)) {
			addField("tenantId", tenantId);
		}
		if (!admin) {
			String ownerId = "";
			for (FieldObject field : fieldList) {
				if ("owner".equals(field.getFieldName()) && StringUtils.isNotEmpty(owner)
						&& StringUtils.isEmpty(field.getFieldValue().toString())) {
					ownerId = field.getFieldValue().toString();
				}
			}
			addField("owner", StringUtils.defaultIfEmpty(ownerId, owner));
		}
		return this;
	}
}
