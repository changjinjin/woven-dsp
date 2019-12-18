package com.info.baymax.common.service.criteria.example;

import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.mybatis.page.IPageable;
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
 * Example Query ,这个对象的属性可以构造一个{@link tk.mybatis.mapper.entity.Example} 对象
 *
 * @author jingwei.yang
 * @date 2019年6月27日 上午10:08:57
 */
@ApiModel
@Setter
@Getter
@ToString
public class ExampleQuery implements QueryBuilder<ExampleQuery>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty("分页信息，默认不设置分页")
    protected IPageable pageable = new IPageable(false);

    @ApiModelProperty(value = "查询数据类型", hidden = true)
    protected Class<?> entityClass;

    @ApiModelProperty("是否去重，默认false")
    protected boolean distinct;

    @ApiModelProperty(value = "是否锁表，默认false", hidden = true)
    protected boolean forUpdate;

    @ApiModelProperty("需要统计的字段名，如：count(id)则该属性为'id'，统计时使用，默认为空")
    protected String countProperty;

    @ApiModelProperty("查询的字段列表")
    protected Set<String> selectProperties;

    @ApiModelProperty("排除的字段列表")
    protected Set<String> excludeProperties;

    @ApiModelProperty("条件规则，多个组合条件的组合")
    protected FieldGroup fieldGroup;

    @ApiModelProperty("排序属性信息")
    protected List<Sort> ordSort;

    @ApiModelProperty(value = "动态表名，可以是一个表名，也可以是一个子查询的结果表，如：(select * from user) u", hidden = true)
    protected String dynamicTable;

    @ApiModelProperty(value = "追加表名，一个sql拼接片段端，如：t inner join user_role_ref urr on t.id = urr.user_id追加在（select * from t_user）之后用于关联条件用", hidden = true)
    protected String appendTable;

    @ApiModelProperty(value = "表别名", hidden = true)
    protected String tableAlias;

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

    public ExampleQuery(FieldGroup fieldGroup, List<Sort> ordSort) {
        super();
        this.fieldGroup = fieldGroup;
        this.ordSort = ordSort;
    }

    public ExampleQuery(String[] selectProperties, String[] excludeProperties, FieldGroup fieldGroup,
                        String orderByClause) {
        if (selectProperties != null && selectProperties.length > 0) {
            this.selectProperties = new HashSet<>(Arrays.asList(selectProperties));
        }
        if (excludeProperties != null && excludeProperties.length > 0) {
            this.excludeProperties = new HashSet<>(Arrays.asList(excludeProperties));
        }
        this.fieldGroup = fieldGroup;
    }

    public ExampleQuery(Set<String> selectProperties, Set<String> excludeProperties, FieldGroup fieldGroup,
                        List<Sort> ordSort) {
        super();
        this.selectProperties = selectProperties;
        this.excludeProperties = excludeProperties;
        this.fieldGroup = fieldGroup;
        this.ordSort = ordSort;
    }

    public ExampleQuery(boolean distinct, boolean forUpdate, String countProperty, Set<String> selectProperties,
                        Set<String> excludeProperties, FieldGroup fieldGroup, List<Sort> ordSort) {
        super();
        this.distinct = distinct;
        this.forUpdate = forUpdate;
        this.countProperty = countProperty;
        this.selectProperties = selectProperties;
        this.excludeProperties = excludeProperties;
        this.fieldGroup = fieldGroup;
        this.ordSort = ordSort;
    }

    public ExampleQuery(boolean pageable, int pageNum, int pageSize, Class<?> entityClass, boolean distinct,
                        boolean forUpdate, String countProperty, Set<String> selectProperties, Set<String> excludeProperties,
                        FieldGroup fieldGroup, List<Sort> ordSort) {
        this.pageable = new IPageable(pageable, pageNum, pageSize);
        this.entityClass = entityClass;
        this.distinct = distinct;
        this.forUpdate = forUpdate;
        this.countProperty = countProperty;
        this.selectProperties = selectProperties;
        this.excludeProperties = excludeProperties;
        this.fieldGroup = fieldGroup;
        this.ordSort = ordSort;
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
        setPageable(pageable);
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
        setPageable(IPageable.page(pageNum, pageSize));
        return this;
    }

    @Override
    public ExampleQuery offset(int offset, int limit) {
        setPageable(IPageable.offset(offset, limit));
        return this;
    }

    @Override
    public ExampleQuery distinct(boolean distinct) {
        setDistinct(distinct);
        return this;
    }

    @Override
    public ExampleQuery forUpdate(boolean forUpdate) {
        setForUpdate(forUpdate);
        return this;
    }

    @Override
    public ExampleQuery dynamic(String dynamicTable) {
        setDynamicTable(dynamicTable);
        return this;
    }

    @Override
    public ExampleQuery append(String appendTable) {
        setAppendTable(appendTable);
        return this;
    }

    @Override
    public ExampleQuery tableAlias(String tableAlias) {
        setTableAlias(tableAlias);
        return this;
    }

    @Override
    public ExampleQuery countProperty(String countProperty) {
        setCountProperty(countProperty);
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
            setSelectProperties(selects);
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
            setExcludeProperties(excludes);
        }
        return this;
    }

    @Override
    public FieldGroup fieldGroup() {
        if (this.fieldGroup == null) {
            this.fieldGroup = FieldGroup.builder(AndOr.AND);
            fieldGroup.setQuery(this);
            setFieldGroup(fieldGroup);
        } else {
            fieldGroup.setQuery(this);
        }
        return fieldGroup;
    }

    @Override
    public ExampleQuery fieldGroup(FieldGroup fieldGroup) {
        if (this.fieldGroup == null) {
            setFieldGroup(fieldGroup);
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
            setOrdSort(ordSort2);
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
}
