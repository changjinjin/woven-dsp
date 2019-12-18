package com.info.baymax.common.service.criteria.example;

import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.example.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.example.SqlEnums.Operator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 一个查询条件包装对象
 *
 * @author jingwei.yang
 * @date 2019年9月6日 下午12:17:52
 */
@ApiModel
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Field extends CriteriaItem {
    private static final long serialVersionUID = -1405405956112124994L;

    /**
     * 条件关联类型，默认：AND
     */
    @ApiModelProperty("条件关联类型：AND或OR，默认：AND")
    private AndOr andOr = AndOr.AND;

    /**
     * 属性名称（java字段名称，非数据库列名）
     */
    @ApiModelProperty("属性名称：java实体类中字段名称，非数据库列名")
    private String name;

    @ApiModelProperty(value = "比较操作类型，默认：EQUAL")
    private Operator oper = Operator.EQUAL;

    @ApiModelProperty(//
        "属性值数组:"//
            + "1）单值比较时数组长度为1，如：EQUAL, NOT_EQUAL, LIKE, NOT_LIKE, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL；"//
            + "2）多值比较时数组长度为实际长度，如： BETWEEN, NOT_BETWEEN, IN, NOT_IN；"//
            + "3）判断字段是否为空时该值可为空，如： NULL, NOT_NULL。"//
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
