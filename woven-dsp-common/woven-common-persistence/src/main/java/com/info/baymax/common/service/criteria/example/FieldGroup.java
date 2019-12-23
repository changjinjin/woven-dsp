package com.info.baymax.common.service.criteria.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.FieldGroupBuilder;
import com.info.baymax.common.service.criteria.example.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.example.SqlEnums.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.reflection.MetaObject;
import tk.mybatis.mapper.util.MetaObjectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 条件分组包装对象，多条件或多层条件时需要通过组合模式进行包装处理
 *
 * @author jingwei.yang
 * @date 2019年9月6日 下午12:22:41
 */
@ApiModel
@Setter
@Getter
@ToString
public class FieldGroup extends CriteriaItem implements FieldGroupBuilder<FieldGroup> {
    private static final long serialVersionUID = 2462883877776169902L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected int counter = 0;

    @ApiModelProperty(value = "条件关联类型：AND或OR，默认：AND", allowableValues = "AND,OR")
    protected AndOr andOr;

    @ApiModelProperty("简单条件集合")
    protected List<Field> feilds;

    @ApiModelProperty("条件分组集合")
    protected List<FieldGroup> fieldGroups;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected FieldGroup parent;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected ExampleQuery query;

    /************************* 建造器 ******************************/
    public static FieldGroup builder() {
        return new FieldGroup();
    }

    public static FieldGroup builder(AndOr andOr) {
        return new FieldGroup(andOr);
    }

    /************************* 构造器 ******************************/
    public FieldGroup() {
        this(AndOr.AND);
    }

    public FieldGroup(AndOr andOr) {
        this.andOr = andOr;
    }

    public FieldGroup(AndOr andOr, List<Field> feilds, List<FieldGroup> fieldGroups) {
        this.andOr = andOr;
        this.feilds = feilds;
        this.fieldGroups = fieldGroups;
    }

    public ExampleQuery getQuery() {
        if (query == null && parent != null) {
            query = parent.getQuery();
        }
        return query;
    }

    /************************* 排序节点 ******************************/
    public List<CriteriaItem> ordItems() {
        List<CriteriaItem> items = new ArrayList<CriteriaItem>();
        if (feilds != null && !feilds.isEmpty()) {
            items.addAll(feilds);
        }
        if (fieldGroups != null && !fieldGroups.isEmpty()) {
            items.addAll(fieldGroups);
        }
        Collections.sort(items);// 排序
        return items;
    }

    /************************* 组装条件 ******************************/
    @Override
    public FieldGroup feilds(List<Field> fields) {
        if (fields != null) {
            for (Field field : fields) {
                field(field);
            }
        }
        return this;
    }

    @Override
    public FieldGroup feilds(Field... fields) {
        return feilds(Arrays.asList(fields));
    }

    @Override
    public FieldGroup field(Field field) {
        if (feilds == null) {
            feilds = new ArrayList<>();
        }

        if (field != null) {
            counter++;
            field.setIndex(counter);
            this.feilds.add(field);
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
        andLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup andRightLike(String property, String value) {
        andLike(property, "%".concat(value));
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
        andNotLike(property, value.concat("%"));
        return this;
    }

    @Override
    public FieldGroup andNotRightLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return this;
    }

    @Override
    public FieldGroup andNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return this;
    }

    @Override
    public FieldGroup andEqualTo(Object param) {
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
    public FieldGroup andAllEqualTo(Object param) {
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
    public FieldGroup orAllEqualTo(Object param) {
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
    public FieldGroup andOr(AndOr andOr) {
        setAndOr(andOr);
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
            group.setParent(this);
            group.setQuery(this.getQuery());
            this.fieldGroups.add(group);
        }
        return this;
    }

    @Override
    public FieldGroup andGroup(FieldGroup group) {
        group.setAndOr(AndOr.AND);
        return group(group);
    }

    @Override
    public FieldGroup orGroup(FieldGroup group) {
        group.setAndOr(AndOr.OR);
        return group(group);
    }

    public ExampleQuery end() {
        return this.query;
    }
}
