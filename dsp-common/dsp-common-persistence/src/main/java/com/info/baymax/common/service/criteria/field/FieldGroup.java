package com.info.baymax.common.service.criteria.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.field.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.field.SqlEnums.Operator;
import com.info.baymax.common.service.criteria.query.QueryBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.util.*;

/**
 * 条件分组包装对象，多条件或多层条件时需要通过组合模式进行包装处理
 *
 * @author jingwei.yang
 * @date 2019年9月6日 下午12:22:41
 */
@ApiModel
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters = true)
@SuppressWarnings("unchecked")
public class FieldGroup<T extends QueryBuilder<T>> extends CriteriaItem implements FieldGroupBuilder<T, FieldGroup<T>> {
    private static final long serialVersionUID = 2462883877776169902L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected int counter = 0;

    @ApiModelProperty(value = "条件关联类型：AND或OR，默认：AND", allowableValues = "AND,OR")
    protected AndOr andOr;

    @ApiModelProperty("简单条件集合")
    protected List<Field> fields;

    @ApiModelProperty("条件分组集合")
    protected List<FieldGroup<T>> fieldGroups;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected FieldGroup<T> parent;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected T query;

    /************************* 建造器 ******************************/
    public static <Q extends QueryBuilder<Q>> FieldGroup<Q> builder() {
        return new FieldGroup<Q>();
    }

    public static <Q extends QueryBuilder<Q>> FieldGroup<Q> builder(AndOr andOr) {
        return new FieldGroup<Q>(andOr);
    }

    /************************* 构造器 ******************************/
    private FieldGroup() {
        this(AndOr.AND);
    }

    private FieldGroup(AndOr andOr) {
        this.andOr = andOr;
    }

    private FieldGroup(AndOr andOr, List<Field> fields, List<FieldGroup<T>> fieldGroups) {
        this.andOr = andOr;
        this.fields = fields;
        this.fieldGroups = fieldGroups;
    }

    public T getQuery() {
        if (query == null && parent != null) {
            query = parent.getQuery();
        }
        return query;
    }

    @Override
    public FieldGroup<T> andOr(AndOr andOr) {
        setAndOr(andOr);
        return this;
    }

    @Override
    public FieldGroup<T> group(FieldGroup<T> group) {
        if (fieldGroups == null) {
            fieldGroups = new ArrayList<>();
        }
        if (group != null) {
            counter++;
            group.setIndex(counter);
            group.setParent(this);
            group.setQuery(this.getQuery());
            this.fieldGroups.add(group);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andGroup(FieldGroup<T> group) {
        group.setAndOr(AndOr.AND);
        return group(group);
    }

    @Override
    public FieldGroup<T> orGroup(FieldGroup<T> group) {
        group.setAndOr(AndOr.OR);
        return group(group);
    }

    @Override
    public FieldGroup<T> removeFields(List<Field> fields) {
        if (this.fields != null && !this.fields.isEmpty()) {
            this.fields.removeAll(fields);
        }
        return this;
    }

    @Override
    public FieldGroup<T> removeFields(Field... fields) {
        return removeFields(Lists.newArrayList(fields));
    }

    @Override
    public FieldGroup<T> removeFields(String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            HashSet<String> newHashSet = Sets.newHashSet(fieldNames);
            Iterator<Field> iterator = this.fields.iterator();
            if (iterator.hasNext()) {
                Field next = iterator.next();
                if (newHashSet.contains(next.getName())) {
                    iterator.remove();
                }
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> removeField(Field field) {
        return removeFields(field);
    }

    @Override
    public FieldGroup<T> removeField(String fieldName) {
        return removeFields(fieldName);
    }

    @Override
    public FieldGroup<T> removeGroup(FieldGroup<T> group) {
        return removeGroups(group);
    }

    @Override
    public FieldGroup<T> removeGroups(FieldGroup<T>... groups) {
        return removeGroups(Lists.newArrayList(groups));
    }

    @Override
    public FieldGroup<T> removeGroups(List<FieldGroup<T>> groups) {
        if (this.fieldGroups != null && !this.fieldGroups.isEmpty()) {
            this.fieldGroups.removeAll(groups);
        }
        return this;
    }

    public T end() {
        return this.query;
    }

    /************************* 排序节点 ******************************/
    public List<CriteriaItem> ordItems() {
        List<CriteriaItem> items = new ArrayList<CriteriaItem>();
        if (fields != null && !fields.isEmpty()) {
            items.addAll(fields);
        }
        if (fieldGroups != null && !fieldGroups.isEmpty()) {
            items.addAll(fieldGroups);
        }
        Collections.sort(items);// 排序
        return items;
    }

    /************************* 组装条件 ******************************/
    @Override
    public FieldGroup<T> reIndex() {
        int i = 0;
        List<CriteriaItem> ordItems = ordItems();
        for (CriteriaItem item : ordItems) {
            item.setIndex(i++);
            if (item instanceof FieldGroup) {
                ((FieldGroup<T>) item).reIndex();
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> fields(List<Field> fields) {
        if (fields != null) {
            for (Field field : fields) {
                field(field);
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> fields(Field... fields) {
        return fields(Lists.newArrayList(fields));
    }

    @Override
    public FieldGroup<T> field(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }

        if (field != null) {
            counter++;
            field.setIndex(counter);
            this.fields.add(field);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andIsNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.IS_NULL));
        return this;
    }

    @Override
    public FieldGroup<T> andIsNotNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_NULL));
        return this;
    }

    @Override
    public FieldGroup<T> andEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> andNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN, value));
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> andLessThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN, value));
        return this;
    }

    @Override
    public FieldGroup<T> andLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> andIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.IN, values));
        return this;
    }

    @Override
    public FieldGroup<T> andNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_IN, values));
        return this;
    }

    @Override
    public FieldGroup<T> andBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup<T> andNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup<T> andLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.LIKE, value));
        return this;
    }

    @Override
    public FieldGroup<T> andLeftLike(String property, String value) {
        andLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup<T> andRightLike(String property, String value) {
        andLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> andFullLike(String property, String value) {
        andLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> andNotLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_LIKE, value));
        return this;
    }

    @Override
    public FieldGroup<T> andNotLeftLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup<T> andNotRightLike(String property, String value) {
        andNotLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> andNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> andEqualTo(Object param) {
        MetaObject metaObject = MetaObjectUtil.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            Object value = metaObject.getValue(property);
            if (value != null) {
                andEqualTo(property, value);
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> andAllEqualTo(Object param) {
        MetaObject metaObject = MetaObjectUtil.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            Object value = metaObject.getValue(property);
            if (value != null) {
                andEqualTo(property, value);
            } else {
                andIsNull(property);
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> orIsNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.IS_NULL));
        return this;
    }

    @Override
    public FieldGroup<T> orIsNotNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_NULL));
        return this;
    }

    @Override
    public FieldGroup<T> orEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> orNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN, value));
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> orLessThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN, value));
        return this;
    }

    @Override
    public FieldGroup<T> orLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup<T> orIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.IN, values));
        return this;
    }

    @Override
    public FieldGroup<T> orNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_IN, values));
        return this;
    }

    @Override
    public FieldGroup<T> orBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup<T> orNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup<T> orLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.LIKE, value));
        return this;
    }

    @Override
    public FieldGroup<T> orLeftLike(String property, String value) {
        andLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> orRightLike(String property, String value) {
        andLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup<T> orFullLike(String property, String value) {
        andLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> orNotLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_LIKE, value));
        return this;
    }

    @Override
    public FieldGroup<T> orNotLeftLike(String property, String value) {
        andNotLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> orNotRightLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup<T> orNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup<T> orEqualTo(Object param) {
        MetaObject metaObject = MetaObjectUtil.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            Object value = metaObject.getValue(property);
            if (value != null) {
                orEqualTo(property, value);
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> orAllEqualTo(Object param) {
        MetaObject metaObject = MetaObjectUtil.forObject(param);
        String[] properties = metaObject.getGetterNames();
        for (String property : properties) {
            Object value = metaObject.getValue(property);
            if (value != null) {
                orEqualTo(property, value);
            } else {
                orIsNull(property);
            }
        }
        return this;
    }

    @Override
    public FieldGroup<T> andEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotLikeifNotNull(String property, String value) {
        if (value != null) {
            return orNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andIsNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNotNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andNotBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup<T> andAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andAllEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orIsNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNotNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orNotBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotFullLike(property, value);
        }
        return this;

    }

    @Override
    public FieldGroup<T> orEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup<T> orAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orAllEqualTo(param);
        }
        return this;
    }
}
