package com.info.baymax.common.queryapi.query.field;

import com.info.baymax.common.queryapi.query.field.SqlEnums.AndOr;
import com.info.baymax.common.queryapi.query.field.SqlEnums.Operator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 一个查询条件包装对象
 *
 * @author jingwei.yang
 * @date 2019年9月6日 下午12:17:52
 */
@ApiModel
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Field extends FieldItem {
    private static final long serialVersionUID = -1405405956112124994L;

    /**
     * 条件关联类型，默认：AND
     */
    @ApiModelProperty(value = "条件关联类型：AND或OR，默认：AND", allowableValues = "AND,OR",required = false)
    private AndOr andOr = AndOr.AND;

    /**
     * 属性名称（java字段名称，非数据库列名）
     */
    @ApiModelProperty(value = "属性名称：java实体类中字段名称，非数据库列名",required = true)
    private String name;

    @ApiModelProperty(value = "比较操作类型，默认：EQUAL", allowableValues = "EQUAL,NOT_EQUAL,LIKE,NOT_LIKE,BETWEEN,NOT_BETWEEN,GREATER_THAN,GREATER_THAN_OR_EQUAL,LESS_THAN,LESS_THAN_OR_EQUAL,IS_NULL,NOT_NULL,IN,NOT_IN",required = true)
    private Operator oper = Operator.EQUAL;

    @ApiModelProperty(value = 
            "属性值数组:"//
            + "1）单值比较时数组长度为1，如：EQUAL, NOT_EQUAL, LIKE, NOT_LIKE, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL；"//
            + "2）多值比较时数组长度为实际长度，如： BETWEEN, NOT_BETWEEN, IN, NOT_IN；"//
            + "3）判断字段是否为空时该值可为空，如： NULL, NOT_NULL。",//
            required = false
    )
    private Object[] value;

    public static Field apply(AndOr andOr, String name, Operator operator) {
        return new Field(andOr, name, operator, new Object[]{});
    }

    public static Field apply(AndOr andOr, String name, Operator operator, Object value) {
        return new Field(andOr, name, operator, new Object[]{value});
    }

    public static Field apply(AndOr andOr, String name, Operator operator, Object... value) {
        return new Field(andOr, name, operator, value);
    }
}
