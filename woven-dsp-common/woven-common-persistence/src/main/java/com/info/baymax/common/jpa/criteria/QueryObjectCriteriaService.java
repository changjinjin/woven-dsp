package com.info.baymax.common.jpa.criteria;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.info.baymax.common.service.criteria.ExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.Field;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.common.service.criteria.example.Sort;
import com.info.baymax.common.service.criteria.example.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.example.SqlEnums.Operator;
import com.info.baymax.common.service.criteria.example.SqlEnums.OrderType;
import com.info.baymax.common.jpa.criteria.query.FieldObject;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.ComparatorOperator;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.LogicalOperator;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.OrderDirection;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.criteria.query.SortObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.mybatis.page.IPage;
import com.info.baymax.common.utils.EntityUtils;
import com.info.baymax.common.utils.ICollections;

/**
 * 通过组合Criteria查询数据方法定义，一般用于一些复杂条件的数据筛选查询
 *
 * @author jingwei.yang
 * @date 2019年5月7日 上午10:11:38
 * @param <T>
 */
// FIXME JPA 到 Mybatis 过渡接口，全部替换为mybatis后这些接口建议弃用
@Deprecated
public interface QueryObjectCriteriaService<T> extends ExampleQueryService<T> {

	/**
	 * 根据动态查询条件查询
	 *
	 * @param queryObject 查询条件
	 * @return 分页数据集
	 * @see com.info.baymax.common.service.criteria.ExampleQueryService#selectPage(com.info.baymax.common.service.criteria.example.ExampleQuery)
	 */
	default Page<T> findObjectPage(QueryObject queryObject) {
		return findObjectPage(exampleQuery(queryObject));
	}

	/**
	 * 根据ExampleQuery查询返回一个JPA的Page对象，用于公共调用
	 *
	 * @param query 查询条件
	 * @return 分页数据集
	 * @see com.info.baymax.common.service.criteria.ExampleQueryService#selectPage(com.info.baymax.common.service.criteria.example.ExampleQuery)
	 */
	default Page<T> findObjectPage(ExampleQuery query) {
		IPage<T> selectPage = selectPage(query);
		if (selectPage != null) {
			return new Page<T>(selectPage.getPageNum(), selectPage.getPageSize(), selectPage.getTotalCount(),
					selectPage.getList());
		}
		return null;
	}

	/**
	 * 根据查询条件获取所有结果
	 *
	 * @param queryObject 查询条件
	 * @return 查询的结果集
	 * @see com.info.baymax.common.service.criteria.ExampleQueryService#selectList(com.info.baymax.common.service.criteria.example.ExampleQuery)
	 */
	default List<T> findList(QueryObject queryObject) {
		return selectList(exampleQuery(queryObject));
	}

	/**
	 * 根据查询条件获取单个结果
	 *
	 * @param queryObject 查询条件
	 * @return 查询的单条结果
	 * @see com.info.baymax.common.service.criteria.ExampleQueryService#selectOne(com.info.baymax.common.service.criteria.example.ExampleQuery)
	 */
	default T getSingleResult(QueryObject queryObject) {
		return selectOne(exampleQuery(queryObject));
	}

	/**
	 * 根据条件删除数据
	 *
	 * @param queryObject 删除条件
	 * @see com.info.baymax.common.service.criteria.ExampleQueryService#delete(com.info.baymax.common.service.criteria.example.ExampleQuery)
	 */
	default int delete(QueryObject queryObject) {
		return delete(exampleQuery(queryObject));
	}

	/**
	 * 根据QueryObject对象转化为ExampleQuery对象
	 *
	 * @param queryObject QueryObject条件包装
	 * @return 转化后的ExampleQuery对象
	 */
	default ExampleQuery exampleQuery(QueryObject queryObject) {
		return ExampleQueryTransferHelper.queryObjectToExampleQuery(queryObject, getEntityClass());
	}

}

/**
 * QueryObject对象到ExampleQuery对象转化辅助类，QueryObject相关方法已经弃用，推荐使用ExampleQuery相关的查询API，这里是为了暂时兼容原来的JPA版本做的过渡逻辑
 *
 * @author jingwei.yang
 * @date 2019年8月23日 上午9:49:59
 */
class ExampleQueryTransferHelper {

	/**
	 * 条件转化
	 *
	 * @param queryObject 原来的JPA QueryObject条件
	 * @return Mybatis的ExampleQuery查询条件
	 */
	public static ExampleQuery queryObjectToExampleQuery(QueryObject queryObject, Class<?> entityClass) {
		ExampleQuery query = ExampleQuery.builder(entityClass);
		if (queryObject == null) {
			return query;
		}

		// 分页
		Integer limit = queryObject.getLimit();
		if (limit != null) {
			Integer offset = queryObject.getOffset();
			if (offset == null) {
				offset = 0;
			}
			query.offset(offset, limit);
		}

		// 条件
		List<FieldObject> fieldList = queryObject.getFieldList();
		if (fieldList != null) {
			query.fieldGroup(ExampleQueryTransferHelper.fieldListToFieldGroup(fieldList));//
		}

		// 排序
		SortObject sortObject = queryObject.getSortObject();
		if (sortObject != null) {
			query.sorts(ExampleQueryTransferHelper.sortObjectToSort(sortObject));
		}

		return query;
	}

	public static FieldGroup fieldListToFieldGroup(List<FieldObject> fieldList) {
		FieldGroup group = FieldGroup.builder();
		if (fieldList != null) {
			for (FieldObject fieldObject : fieldList) {
				Field field = fieldObjectToField(fieldObject);
				if (field != null) {
					group.field(field);
				}
			}
		}
		return group;
	}

	// 检查一些不合发的参数或者格式，进行格式化处理
	private static FieldObject checkFieldName(FieldObject fieldObject) {
		if (fieldObject != null) {
			String fieldName = fieldObject.getFieldName();
			Object fieldValue = fieldObject.getFieldValue();
			if ("tenant".equals(fieldName)) {
				Map<String, Object> bean2Map = EntityUtils.transBean2Map(fieldValue);
				Object tenantId = bean2Map.get("id");
				if (tenantId == null) {
					return null;
				}
				fieldObject = new FieldObject("tenantId", tenantId, ComparatorOperator.EQUAL);
			}
			if ("lastModifiedTime".equals(fieldName) || "createTime".equals(fieldName)) {
				if (fieldValue instanceof Long) {
					fieldObject = new FieldObject(fieldName, new Date((long) fieldValue),
							fieldObject.getComparatorOperator(), fieldObject.getLogicalOperator());
				}
			}
		}
		return fieldObject;
	}

	public static Field fieldObjectToField(FieldObject fieldObject) {
		fieldObject = checkFieldName(fieldObject);
		if (fieldObject == null) {
			return null;
		}

		Object obj = fieldObject.getFieldValue();
		if (obj == null) {
			switch (fieldObject.getComparatorOperator()) {
			// 如果比较关系不需要值得时候则添加否则不添加条件
			case NULL:
			case NOT_NULL:
				return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()), fieldObject.getFieldName(),
						comparatorOperatorToOperator(fieldObject.getComparatorOperator()));
			default:
				// 如果参数值为空则忽略改条件
				return null;
			}
		} else {
			Class<? extends Object> clazz = obj.getClass();
			if (Iterable.class.isAssignableFrom(clazz)) {
				List<Object> list = Lists.newArrayList((Iterable<?>) obj);
				if (ICollections.hasElements(list)) {
					return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()),
							fieldObject.getFieldName(),
							comparatorOperatorToOperator(fieldObject.getComparatorOperator()), list.toArray());
				}
			} else if (clazz.isArray()) {
				Object[] arrValues = (Object[]) obj;
				if (arrValues.length > 0) {
					return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()),
							fieldObject.getFieldName(),
							comparatorOperatorToOperator(fieldObject.getComparatorOperator()), arrValues);
				}
			} else {
				return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()), fieldObject.getFieldName(),
						comparatorOperatorToOperator(fieldObject.getComparatorOperator()), new Object[] { obj });
			}
		}
		return null;
	}

	public static Sort sortObjectToSort(SortObject sortObject) {
		OrderDirection orderDirection = sortObject.getOrderDirection();
		switch (orderDirection) {
		case DESC:
			return new Sort(sortObject.getField(), OrderType.DESC);
		default:
			return new Sort(sortObject.getField(), OrderType.ASC);
		}
	}

	public static AndOr logicalOperatorToAndOr(LogicalOperator logicalOperator) {
		switch (logicalOperator) {
		case OR:
			return AndOr.OR;
		default:
			return AndOr.AND;
		}
	}

	public static Operator comparatorOperatorToOperator(ComparatorOperator comparatorOperator) {
		switch (comparatorOperator) {
		case EQUAL:
			return Operator.EQUAL;
		case NOT_EQUAL:
			return Operator.NOT_EQUAL;
		case LIKE:
			return Operator.LIKE;
		case BETWEEN:
			return Operator.BETWEEN;
		case NOT_BETWEEN:
			return Operator.NOT_BETWEEN;
		case GREATER_THAN:
			return Operator.GREATER_THAN;
		case GREATER_THAN_OR_EQUAL:
			return Operator.GREATER_THAN_OR_EQUAL;
		case LESS_THAN:
			return Operator.LESS_THAN;
		case LESS_THAN_OR_EQUAL:
			return Operator.LESS_THAN_OR_EQUAL;
		case NULL:
			return Operator.IS_NULL;
		case NOT_NULL:
			return Operator.NOT_NULL;
		case IN:
			return Operator.IN;
		case NOT_IN:
			return Operator.NOT_IN;
		default:
			return Operator.EQUAL;
		}
	}

}
