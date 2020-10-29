package com.info.baymax.common.queryapi.query.aggregate;

import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.query.AbstractQuery;
import com.info.baymax.common.queryapi.query.field.Field;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.field.FieldItem;
import com.info.baymax.common.queryapi.query.field.Sort;
import com.info.baymax.common.queryapi.query.field.SqlEnums.AndOr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 构建聚合查询条件
 *
 * <pre>
 * 	<code>
 *   SELECT
 *   	project,
 *   	manager,
 *   	code,
 *   	count(income) count_income,
 *   	sum(income) sum_income,
 *   	avg(income) avg_income,
 *   	max(income) max_income,
 *   	min(income) min_income
 *   FROM
 *   	t_p
 *   WHERE
 *     project <> 'px'
 *   GROUP BY
 *   	project, manager, code
 *   HAVING
 *   	( code > '1' AND avg_income > 10 ) OR min_income > 5000
 *   ORDER BY
 *   	min_income, sum_income
 *   LIMIT 1,2
 *  </code>
 * </pre>
 *
 * @param <T> AbstractQuery子类
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:13:02
 */
@ApiModel
@Getter
@SuppressWarnings("unchecked")
public abstract class AbstractAggQuery<T extends AbstractAggQuery<T>> extends AbstractQuery<AbstractAggQuery<T>>
        implements AggFieldQueryBuilder<T>, GroupByFieldQueryBuilder<T>, HavingFieldGroupQueryBuilder<T> {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty("聚合条件")
    protected LinkedHashSet<AggField> aggFields;

    @ApiModelProperty("分组字段")
    protected LinkedHashSet<String> groupFields;

    @ApiModelProperty("having查询条件")
    protected FieldGroup havingFieldGroup;

    public AbstractAggQuery() {
        super();
    }

    @Override
    public T aggFields(Collection<AggField> aggFields) {
        if (aggFields != null && !aggFields.isEmpty()) {
            if (this.aggFields == null) {
                this.aggFields = new LinkedHashSet<>();
            }
            this.aggFields.addAll(aggFields);
        }
        return (T) this;
    }

    @Override
    public T clearAggFields() {
        clear(this.aggFields);
        return (T) this;
    }

    @Override
    public T groupFields(Collection<String> groupFields) {
        if (groupFields != null && !groupFields.isEmpty()) {
            if (this.groupFields == null) {
                this.groupFields = new LinkedHashSet<>();
            }
            this.groupFields.addAll(groupFields);
        }
        return (T) this;
    }

    @Override
    public T clearGroupFields() {
        clear(this.groupFields);
        return (T) this;
    }

    @Override
    public FieldGroup havingFieldGroup() {
        if (this.havingFieldGroup == null) {
            this.havingFieldGroup = FieldGroup.builder(AndOr.AND);
        }
        return havingFieldGroup;
    }

    @Override
    public T havingFieldGroup(FieldGroup fieldGroup) {
        if (this.havingFieldGroup == null) {
            this.havingFieldGroup = fieldGroup;
        } else {
            List<FieldItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (FieldItem item : ordItems) {
                    if (item instanceof Field) {
                        this.havingFieldGroup.field((Field) item);
                    } else {
                        this.havingFieldGroup.group((FieldGroup) item);
                    }
                }
            }
        }
        return (T) this;
    }

    @Override
    public T clearHavingFieldGroup() {
        if (this.havingFieldGroup != null) {
            this.havingFieldGroup = null;
        }
        return (T) this;
    }

    @Override
    public T clear() {
        super.clear();
        clearAggFields();
        clearGroupFields();
        clearHavingFieldGroup();
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
    public T sorts(Sort... sorts) {
        super.sorts(sorts);
        return (T) this;
    }

    @Override
    public T sort(Sort sort) {
        super.sort(sort);
        return (T) this;
    }

    @Override
    public T orderBy(String fieldName, boolean asc) {
        super.orderBy(fieldName, asc);
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
    public T orderByAsc(String... fieldNames) {
        super.orderByAsc(fieldNames);
        return (T) this;
    }

    @Override
    public T orderByDesc(String fieldName) {
        super.orderByDesc(fieldName);
        return (T) this;
    }

    @Override
    public T orderByDesc(String... fieldNames) {
        super.orderByDesc(fieldNames);
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
    public T fieldGroup(FieldGroup fieldGroup) {
        super.fieldGroup(fieldGroup);
        return (T) this;
    }

    @Override
    public T sorts(Collection<Sort> sorts) {
        super.sorts(sorts);
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

    @Override
    public T table(String table) {
        super.table(table);
        return (T) this;
    }

    @Override
    public T clearTable() {
        super.clearTable();
        return (T) this;
    }

}
