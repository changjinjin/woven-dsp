package com.info.baymax.common.service.criteria.query;

import com.google.common.collect.Sets;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.field.Field;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.service.criteria.field.SqlEnums.AndOr;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * AbstractQuery 构建常用的条件结构:条件，排序，分页
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:13:02
 */
@ApiModel
@Setter
@Getter
@ToString
@SuppressWarnings("unchecked")
public abstract class AbstractQuery<T extends AbstractQuery<T>> implements FieldGroupQueryBuilder<T>,
    SortQueryBuilder<T>, PageableQueryBuilder<T>, QueryBuilder<T>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty("条件规则，多个组合条件的组合")
    protected FieldGroup fieldGroup;

    @ApiModelProperty("排序属性信息")
    protected LinkedHashSet<Sort> ordSort;

    @ApiModelProperty("分页信息，默认不设置分页")
    protected IPageable pageable = new IPageable(false);

    public AbstractQuery() {
        super();
    }

    @Override
    public FieldGroup fieldGroup() {
        if (this.fieldGroup == null) {
            this.fieldGroup = FieldGroup.builder(AndOr.AND);
        }
        return fieldGroup;
    }

    @Override
    public T fieldGroup(FieldGroup fieldGroup) {
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
    public T sorts(Collection<Sort> sorts) {
        if (ICollections.hasElements(sorts)) {
            if (this.ordSort == null) {
                this.ordSort = Sets.newLinkedHashSet();
            }
            this.ordSort.addAll(sorts);
        }
        return (T) this;
    }

    @Override
    public T clearSorts() {
        clear(this.ordSort);
        return (T) this;
    }

    @Override
    public T paged(boolean pageable) {
        this.pageable.setPageable(pageable);
        return (T) this;
    }

    @Override
    public T pageable(IPageable pageable) {
        this.pageable = pageable;
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

    /***** clear logic ****/
    @Override
    public T clearPageable() {
        if (this.pageable != null && this.pageable.isPageable()) {
            return unpaged();
        }
        return (T) this;
    }

    @Override
    public T clear() {
        clearPageable();
        clearFieldGroup();
        clearSorts();
        return (T) this;
    }

    /**
     * 获取最终的查询字段列表
     *
     * @param tableAlias 表别名
     * @return 最终查询字段列表
     */
    public abstract List<String> getFinalSelectProperties(String tableAlias);

}
