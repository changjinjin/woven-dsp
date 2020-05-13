package com.info.baymax.common.service.criteria.example;

import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.QueryBuilder;
import com.info.baymax.common.service.criteria.example.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.example.SqlEnums.OrderType;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

/**
 * Example Query ,这个对象的属性可以构造一个{@link com.info.baymax.common.mybatis.mapper.example.Example} 对象
 *
 * @author jingwei.yang
 * @date 2019年6月27日 上午10:08:57
 */
@ApiModel
@Getter
@Setter
@ToString
public class ExampleQuery implements QueryBuilder<ExampleQuery>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty("分页信息，默认不设置分页")
    protected IPageable pageable = new IPageable(false);

    @ApiModelProperty(value = "查询数据类型", hidden = true)
    protected Class<?> entityClass;

    @ApiModelProperty(value = "是否去重，默认false", hidden = true)
    protected boolean distinct;

    @ApiModelProperty(value = "是否锁表，默认false", hidden = true)
    protected boolean forUpdate;

    @ApiModelProperty(value = "需要统计的字段名，如：count(id)则该属性为'id'，统计时使用，默认为空", hidden = true)
    protected String countProperty;

    @ApiModelProperty("查询的字段列表")
    protected Set<String> selectProperties;

    @ApiModelProperty("排除的字段列表")
    protected Set<String> excludeProperties;

    @ApiModelProperty("条件规则，多个组合条件的组合")
    protected FieldGroup fieldGroup;

    @ApiModelProperty("排序属性信息")
    protected List<Sort> ordSort;

    @ApiModelProperty(value = "join sql：即子查询语句组合", hidden = true)
    protected JoinSql joinSql;

    /*************************************
     * 建造器
     *****************************************/
    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static ExampleQuery builder() {
        return new ExampleQuery();
    }

    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static ExampleQuery builder(ExampleQuery query) {
        return query == null ? new ExampleQuery() : query;
    }

    /**
     * 创建一个Query的构建器
     *
     * @param entityClass 指定查询的数据实体类型
     * @return 默认的构建器
     */
    public static ExampleQuery builder(Class<?> entityClass) {
        return new ExampleQuery(entityClass);
    }

    /*************************************
     * 构造器
     *****************************************/
    public ExampleQuery() {
        super();
    }

    public ExampleQuery(Class<?> entityClass) {
        super();
        this.entityClass = entityClass;
    }

    /*************************************
     * 条件组装
     *****************************************/
    @Override
    public ExampleQuery paged() {
        return paged(true);
    }

    @Override
    public ExampleQuery unpaged() {
        return paged(false);
    }

    @Override
    public ExampleQuery pageable(IPageable pageable) {
        this.pageable = pageable;
        return this;
    }

    private ExampleQuery paged(boolean pageable) {
        this.pageable.setPageable(pageable);
        return this;
    }

    @Override
    public ExampleQuery pageNum(int pageNum) {
        this.pageable.setPageNum(pageNum);
        return this;
    }

    @Override
    public ExampleQuery pageSize(int pageSize) {
        this.pageable.setPageSize(pageSize);
        return this;
    }

    @Override
    public ExampleQuery page(int pageNum, int pageSize) {
        return pageable(IPageable.page(pageNum, pageSize));
    }

    @Override
    public ExampleQuery offset(int offset, int limit) {
        return pageable(IPageable.offset(offset, limit));
    }

    @Override
    public ExampleQuery distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    @Override
    public ExampleQuery forUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
        return this;
    }

    @Override
    public ExampleQuery countProperty(String countProperty) {
        this.countProperty = countProperty;
        return this;
    }

    @Override
    public ExampleQuery selectProperties(String... selectProperties) {
        if (selectProperties != null && selectProperties.length > 0) {
            Set<String> selects = getSelectProperties();
            if (selects == null) {
                selects = new HashSet<>();
            }
            for (String select : selectProperties) {
                selects.add(select);
            }
            this.selectProperties = selects;
        }
        return this;
    }

    @Override
    public ExampleQuery excludeProperties(String... excludeProperties) {
        if (excludeProperties != null && excludeProperties.length > 0) {
            Set<String> excludes = getExcludeProperties();
            if (excludes == null) {
                excludes = new HashSet<>();
            }
            for (String exclude : excludeProperties) {
                excludes.add(exclude);
            }
            this.excludeProperties = excludes;
        }
        return this;
    }

    @Override
    public FieldGroup fieldGroup() {
        if (this.fieldGroup == null) {
            this.fieldGroup = FieldGroup.builder(AndOr.AND);
            fieldGroup.setQuery(this);
        } else {
            fieldGroup.setQuery(this);
        }
        return fieldGroup;
    }

    @Override
    public ExampleQuery fieldGroup(FieldGroup fieldGroup) {
        if (this.fieldGroup == null) {
            this.fieldGroup = fieldGroup;
        } else {
            List<CriteriaItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (CriteriaItem item : ordItems) {
                    if (item instanceof Field) {
                        this.fieldGroup.field((Field) item);
                    } else {
                        this.fieldGroup.group((FieldGroup) item);
                    }
                }
            }
        }
        return this;
    }

    private ExampleQuery ordSort(List<Sort> ordSort) {
        if (ICollections.hasElements(ordSort)) {
            List<Sort> ordSort2 = getOrdSort();
            if (ordSort2 == null) {
                ordSort2 = new ArrayList<Sort>();
            }
            ordSort2.addAll(ordSort);
            this.ordSort = ordSort2;
        }
        return this;
    }

    @Override
    public ExampleQuery sort(Sort ordSort) {
        return ordSort(Arrays.asList(ordSort));
    }

    @Override
    public ExampleQuery orderBy(String fieldName) {
        return sort(fieldName, true);
    }

    private ExampleQuery sort(String fieldName, boolean order) {
        return sort(Sort.apply(fieldName, order ? OrderType.ASC : OrderType.DESC));
    }

    @Override
    public ExampleQuery orderByAsc(String fieldName) {
        return sort(fieldName, true);
    }

    @Override
    public ExampleQuery orderByDesc(String fieldName) {
        return sort(fieldName, false);
    }

    @Override
    public ExampleQuery sorts(Sort... ordSort) {
        return ordSort(Arrays.asList(ordSort));
    }

    @Override
    public ExampleQuery joinSql(JoinSql joinSql) {
        this.joinSql = joinSql;
        return this;
    }

    /***** clear logic ****/
    @Override
    public ExampleQuery clear() {
        clearPageable();
        clearDistinct();
        clearForUpdate();
        clearCountProperty();
        clearSelectProperties();
        clearExcludeProperties();
        clearFieldGroup();
        clearSorts();
        clearJoinSql();
        return this;
    }

    @Override
    public ExampleQuery clearPageable() {
        if (this.pageable != null && this.pageable.isPageable()) {
            return unpaged();
        }
        return this;
    }

    @Override
    public ExampleQuery clearDistinct() {
        if (this.distinct) {
            this.distinct = false;
        }
        return this;
    }

    @Override
    public ExampleQuery clearForUpdate() {
        if (this.forUpdate) {
            this.forUpdate = false;
        }
        return this;
    }

    @Override
    public ExampleQuery clearCountProperty() {
        if (this.countProperty != null) {
            this.countProperty = null;
        }
        return this;
    }

    @Override
    public ExampleQuery clearSelectProperties() {
        if (this.selectProperties != null) {
            this.selectProperties.clear();
        }
        return this;
    }

    @Override
    public ExampleQuery clearExcludeProperties() {
        if (this.excludeProperties != null) {
            this.excludeProperties.clear();
        }
        return this;
    }

    @Override
    public ExampleQuery clearFieldGroup() {
        if (this.fieldGroup != null) {
            this.fieldGroup = null;
        }
        return this;
    }

    @Override
    public ExampleQuery clearSorts() {
        if (this.ordSort != null) {
            this.ordSort.clear();
        }
        return this;
    }

    @Override
    public ExampleQuery clearJoinSql() {
        this.joinSql = null;
        return this;
    }

}
