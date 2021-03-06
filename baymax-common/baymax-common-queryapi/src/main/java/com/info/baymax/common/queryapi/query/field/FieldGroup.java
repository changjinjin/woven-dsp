package com.info.baymax.common.queryapi.query.field;

import com.info.baymax.common.queryapi.query.field.SqlEnums.AndOr;
import com.info.baymax.common.queryapi.query.field.SqlEnums.Operator;
import com.info.baymax.common.queryapi.utils.MapUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.*;

/**
 * 条件分组包装对象，多条件或多层条件时需要通过组合模式进行包装处理
 *
 * @author jingwei.yang
 * @date 2019年9月6日 下午12:22:41
 */
@ApiModel
@Getter
public class FieldGroup extends FieldItem implements FieldGroupBuilder<FieldGroup> {
    private static final long serialVersionUID = 2462883877776169902L;

    @ApiModelProperty(hidden = true)
    protected int counter = 0;

    @ApiModelProperty(value = "条件关联类型：AND或OR，默认：AND", allowableValues = "AND,OR", required = false)
    protected AndOr andOr;

    @ApiModelProperty(value = "简单条件集合", required = false)
    protected List<Field> fields;

    @ApiModelProperty(value = "条件分组集合", required = false)
    protected List<FieldGroup> fieldGroups;

    /************************* 建造器 ******************************/
    public static FieldGroup builder() {
        return new FieldGroup();
    }

    public static FieldGroup builder(AndOr andOr) {
        return new FieldGroup(andOr);
    }

    /************************* 构造器 ******************************/
    private FieldGroup() {
        this(AndOr.AND);
    }

    private FieldGroup(AndOr andOr) {
        this.andOr = andOr;
    }

    private FieldGroup(AndOr andOr, List<Field> fields, List<FieldGroup> fieldGroups) {
        this.andOr = andOr;
        this.fields = fields;
        this.fieldGroups = fieldGroups;
    }

    @Override
    public FieldGroup andOr(AndOr andOr) {
        this.andOr = andOr;
        return this;
    }

    @Override
    public FieldGroup group(FieldGroup group) {
        if (fieldGroups == null) {
            fieldGroups = new ArrayList<>();
        }
        if (group != null) {
            counter++;
            group.setIndex(counter);
            // group.setParent(this);
            this.fieldGroups.add(group);
        }
        return this;
    }

    @Override
    public FieldGroup andGroup(FieldGroup group) {
        group.andOr(AndOr.AND);
        return group(group);
    }

    @Override
    public FieldGroup orGroup(FieldGroup group) {
        group.andOr(AndOr.OR);
        return group(group);
    }

    @Override
    public FieldGroup removeFields(List<Field> fields) {
        if (this.fields != null && !this.fields.isEmpty()) {
            this.fields.removeAll(fields);
        }
        return this;
    }

    @Override
    public FieldGroup removeFields(Field... fields) {
        return removeFields(Arrays.asList(fields));
    }

    @Override
    public FieldGroup removeFields(String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            HashSet<String> newHashSet = new HashSet<String>();
            newHashSet.addAll(Arrays.asList(fieldNames));
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
    public FieldGroup removeField(Field field) {
        return removeFields(field);
    }

    @Override
    public FieldGroup removeField(String fieldName) {
        return removeFields(fieldName);
    }

    @Override
    public FieldGroup removeGroup(FieldGroup group) {
        return removeGroups(group);
    }

    @Override
    public FieldGroup removeGroups(FieldGroup... groups) {
        return removeGroups(Arrays.asList(groups));
    }

    @Override
    public FieldGroup removeGroups(List<FieldGroup> groups) {
        if (this.fieldGroups != null && !this.fieldGroups.isEmpty()) {
            this.fieldGroups.removeAll(groups);
        }
        return this;
    }

    /************************* 排序节点 ******************************/
    public List<FieldItem> ordItems() {
        List<FieldItem> items = new ArrayList<FieldItem>();
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
    public FieldGroup reIndex() {
        int i = 0;
        List<FieldItem> ordItems = ordItems();
        for (FieldItem item : ordItems) {
            item.setIndex(i++);
            if (item instanceof FieldGroup) {
                ((FieldGroup) item).reIndex();
            }
        }
        return this;
    }

    @Override
    public FieldGroup fields(List<Field> fields) {
        if (fields != null) {
            for (Field field : fields) {
                field(field);
            }
        }
        return this;
    }

    @Override
    public FieldGroup fields(Field... fields) {
        return fields(Arrays.asList(fields));
    }

    @Override
    public FieldGroup field(Field field) {
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
    public FieldGroup andIsNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.IS_NULL));
        return this;
    }

    @Override
    public FieldGroup andIsNotNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_NULL));
        return this;
    }

    @Override
    public FieldGroup andEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup andNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup andGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN, value));
        return this;
    }

    @Override
    public FieldGroup andGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup andLessThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN, value));
        return this;
    }

    @Override
    public FieldGroup andLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup andIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.IN, values));
        return this;
    }

    @Override
    public FieldGroup andNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_IN, values));
        return this;
    }

    @Override
    public FieldGroup andBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup andNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup andLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.LIKE, value));
        return this;
    }

    @Override
    public FieldGroup andLeftLike(String property, String value) {
        andLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup andRightLike(String property, String value) {
        andLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup andFullLike(String property, String value) {
        andLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup andNotLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_LIKE, value));
        return this;
    }

    @Override
    public FieldGroup andNotLeftLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup andNotRightLike(String property, String value) {
        andNotLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup andNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup andEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                andEqualTo(k, v);
            }
        });
        return this;
    }

    @Override
    public FieldGroup andAllEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                andEqualTo(k, v);
            } else {
                andIsNull(k);
            }
        });
        return this;
    }

    @Override
    public FieldGroup orIsNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.IS_NULL));
        return this;
    }

    @Override
    public FieldGroup orIsNotNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_NULL));
        return this;
    }

    @Override
    public FieldGroup orEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup orNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup orGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN, value));
        return this;
    }

    @Override
    public FieldGroup orGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup orLessThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN, value));
        return this;
    }

    @Override
    public FieldGroup orLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN_OR_EQUAL, value));
        return this;
    }

    @Override
    public FieldGroup orIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.IN, values));
        return this;
    }

    @Override
    public FieldGroup orNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_IN, values));
        return this;
    }

    @Override
    public FieldGroup orBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup orNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_BETWEEN, value1, value2));
        return this;
    }

    @Override
    public FieldGroup orLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.LIKE, value));
        return this;
    }

    @Override
    public FieldGroup orLeftLike(String property, String value) {
        andLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup orRightLike(String property, String value) {
        andLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup orFullLike(String property, String value) {
        andLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup orNotLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_LIKE, value));
        return this;
    }

    @Override
    public FieldGroup orNotLeftLike(String property, String value) {
        andNotLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup orNotRightLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup orNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup orEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                orEqualTo(k, v);
            }
        });
        return this;
    }

    @Override
    public FieldGroup orAllEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                orEqualTo(k, v);
            } else {
                orIsNull(k);
            }
        });
        return this;
    }

    @Override
    public FieldGroup andEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup andNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup andLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup orNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup orLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotLikeifNotNull(String property, String value) {
        if (value != null) {
            return orNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andIsNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup andIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNotNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup andEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup andNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup andBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup andNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andNotBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup andLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup andEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup andAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andAllEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup orIsNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup orIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNotNull(property);
        }
        return this;
    }

    @Override
    public FieldGroup orEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orNotEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThan(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThanOrEqualTo(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup orNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orNotIn(property, values);
        }
        return this;
    }

    @Override
    public FieldGroup orBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup orNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orNotBetween(property, value1, value2);
        }
        return this;
    }

    @Override
    public FieldGroup orLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orFullLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLeftLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotRightLike(property, value);
        }
        return this;
    }

    @Override
    public FieldGroup orNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotFullLike(property, value);
        }
        return this;

    }

    @Override
    public FieldGroup orEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orEqualTo(param);
        }
        return this;
    }

    @Override
    public FieldGroup orAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orAllEqualTo(param);
        }
        return this;
    }
}
