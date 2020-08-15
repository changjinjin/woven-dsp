package com.info.baymax.common.queryapi.query;

import com.info.baymax.common.queryapi.page.IPageable;
import com.info.baymax.common.queryapi.query.field.Field;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.field.FieldItem;
import com.info.baymax.common.queryapi.query.field.Sort;
import com.info.baymax.common.queryapi.query.field.SqlEnums.AndOr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

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
@Getter
@SuppressWarnings("unchecked")
public abstract class AbstractQuery<T extends AbstractQuery<T>> extends PageableQuery<AbstractQuery<T>>
        implements FieldGroupQueryBuilder<T>, SortQueryBuilder<T>, TableQueryBuilder<T>, QueryBuilder<T>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty(value = "表名称", hidden = true)
    private String table;

    @ApiModelProperty("条件规则，多个组合条件的组合")
    protected FieldGroup fieldGroup;

    @ApiModelProperty("排序属性信息")
    protected LinkedHashSet<Sort> ordSort;

    public AbstractQuery() {
        super();
    }

    public T table(String table) {
        this.table = table;
        return (T) this;
    }

    @Override
    public T clearTable() {
        this.table = null;
        return (T) this;
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
            List<FieldItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (FieldItem item : ordItems) {
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
        if (sorts != null && !sorts.isEmpty()) {
            if (this.ordSort == null) {
                this.ordSort = new LinkedHashSet<>();
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
        return (T) super.paged(pageable);
    }

    @Override
    public T pageable(IPageable pageable) {
        return (T) super.pageable(pageable);
    }

    @Override
    public T pageNum(int pageNum) {
        return (T) super.pageNum(pageNum);
    }

    @Override
    public T pageSize(int pageSize) {
        return (T) super.pageSize(pageSize);
    }

    @Override
    public T clearPageable() {
        return (T) super.clearPageable();
    }

    @Override
    public T clear() {
        clearTable();
        clearPageable();
        clearFieldGroup();
        clearSorts();
        return (T) this;
    }

    /**
     * 获取最终的查询字段列表
     *
     * @return 最终查询字段列表
     */
    public abstract List<String> getFinalSelectProperties();

}
