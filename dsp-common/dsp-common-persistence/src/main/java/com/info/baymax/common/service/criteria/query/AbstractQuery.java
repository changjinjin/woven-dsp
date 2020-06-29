package com.info.baymax.common.service.criteria.query;

import com.google.common.collect.Sets;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.field.Field;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.service.criteria.field.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.field.SqlEnums.OrderType;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
public abstract class AbstractQuery<T extends AbstractQuery<T>> implements QueryBuilder<T>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty("分页信息，默认不设置分页")
    protected IPageable pageable = new IPageable(false);

    @ApiModelProperty(value = "是否去重，默认false", hidden = true)
    protected boolean distinct;

    @ApiModelProperty("查询的字段列表")
    protected Set<String> selectProperties;

    @ApiModelProperty("排除的字段列表")
    protected Set<String> excludeProperties;

    @ApiModelProperty("条件规则，多个组合条件的组合")
    protected FieldGroup<T> fieldGroup;

    @ApiModelProperty("排序属性信息")
    protected List<Sort> ordSort;

    /*************************************
     * 构造器
     *****************************************/
    public AbstractQuery() {
        super();
    }

    /*************************************
     * 条件组装
     *****************************************/
    @Override
    public T paged() {
        return paged(true);
    }

    @Override
    public T unpaged() {
        return paged(false);
    }

    @Override
    public T pageable(IPageable pageable) {
        this.pageable = pageable;
        return (T) this;
    }

    private T paged(boolean pageable) {
        this.pageable.setPageable(pageable);
        return (T) this;
    }

    @Override
    public T pageNum(int pageNum) {
        this.pageable.setPageNum(pageNum);
        return (T) this;
    }

    @Override
    public T pageSize(int pageSize) {
        this.pageable.setPageSize(pageSize);
        return (T) this;
    }

    @Override
    public T page(int pageNum, int pageSize) {
        return pageable(IPageable.page(pageNum, pageSize));
    }

    @Override
    public T offset(int offset, int limit) {
        return pageable(IPageable.offset(offset, limit));
    }

    @Override
    public T distinct(boolean distinct) {
        this.distinct = distinct;
        return (T) this;
    }

    @Override
    public T selectProperties(String... selectProperties) {
        if (selectProperties != null && selectProperties.length > 0) {
            Set<String> selects = getSelectProperties();
            if (selects == null) {
                selects = Sets.newLinkedHashSet();
            }
            for (String select : selectProperties) {
                selects.add(select);
            }
            this.selectProperties = selects;
        }
        return (T) this;
    }

    @Override
    public T excludeProperties(String... excludeProperties) {
        if (excludeProperties != null && excludeProperties.length > 0) {
            Set<String> excludes = getExcludeProperties();
            if (excludes == null) {
                excludes = Sets.newLinkedHashSet();
            }
            for (String exclude : excludeProperties) {
                excludes.add(exclude);
            }
            this.excludeProperties = excludes;
        }
        return (T) this;
    }

    @Override
    public FieldGroup<T> fieldGroup() {
        if (this.fieldGroup == null) {
            this.fieldGroup = FieldGroup.<T>builder(AndOr.AND);
            fieldGroup.setQuery((T) this);
        } else {
            fieldGroup.setQuery((T) this);
        }
        return fieldGroup;
    }

    @Override
    public T fieldGroup(FieldGroup<T> fieldGroup) {
        if (this.fieldGroup == null) {
            this.fieldGroup = fieldGroup;
        } else {
            List<CriteriaItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (CriteriaItem item : ordItems) {
                    if (item instanceof Field) {
                        this.fieldGroup.field((Field) item);
                    } else {
                        this.fieldGroup.group((FieldGroup<T>) item);
                    }
                }
            }
        }
        return (T) this;
    }

    private T ordSort(List<Sort> ordSort) {
        if (ICollections.hasElements(ordSort)) {
            List<Sort> ordSort2 = getOrdSort();
            if (ordSort2 == null) {
                ordSort2 = new ArrayList<Sort>();
            }
            ordSort2.addAll(ordSort);
            this.ordSort = ordSort2;
        }
        return (T) this;
    }

    @Override
    public T sort(Sort ordSort) {
        return ordSort(Arrays.asList(ordSort));
    }

    @Override
    public T orderBy(String fieldName) {
        return sort(fieldName, true);
    }

    private T sort(String fieldName, boolean order) {
        return sort(Sort.apply(fieldName, order ? OrderType.ASC : OrderType.DESC));
    }

    @Override
    public T orderByAsc(String fieldName) {
        return sort(fieldName, true);
    }

    @Override
    public T orderByDesc(String fieldName) {
        return sort(fieldName, false);
    }

    @Override
    public T sorts(Sort... ordSort) {
        return ordSort(Arrays.asList(ordSort));
    }

    /***** clear logic ****/
    @Override
    public T clear() {
        clearPageable();
        clearDistinct();
        clearSelectProperties();
        clearExcludeProperties();
        clearFieldGroup();
        clearSorts();
        return (T) this;
    }

    @Override
    public T clearPageable() {
        if (this.pageable != null && this.pageable.isPageable()) {
            return unpaged();
        }
        return (T) this;
    }

    @Override
    public T clearDistinct() {
        if (this.distinct) {
            this.distinct = false;
        }
        return (T) this;
    }

    @Override
    public T clearSelectProperties() {
        if (this.selectProperties != null) {
            this.selectProperties.clear();
        }
        return (T) this;
    }

    @Override
    public T clearExcludeProperties() {
        if (this.excludeProperties != null) {
            this.excludeProperties.clear();
        }
        return (T) this;
    }

    @Override
    public T clearFieldGroup() {
        if (this.fieldGroup != null) {
            this.fieldGroup = null;
        }
        return (T) this;
    }

    @Override
    public T clearSorts() {
        if (this.ordSort != null) {
            this.ordSort.clear();
        }
        return (T) this;
    }

}
