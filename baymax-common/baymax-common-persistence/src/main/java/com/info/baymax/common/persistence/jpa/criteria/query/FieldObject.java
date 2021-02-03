package com.info.baymax.common.persistence.jpa.criteria.query;

import com.info.baymax.common.persistence.jpa.criteria.query.JpaCriteriaHelper.ComparatorOperator;
import com.info.baymax.common.persistence.jpa.criteria.query.JpaCriteriaHelper.LogicalOperator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 属性信息
 *
 * @author jingwei.yang
 * @date 2019年5月5日 下午12:05:11
 */
@Data
@NoArgsConstructor
@ApiModel
@Deprecated
public class FieldObject implements Serializable {
    private static final long serialVersionUID = -1619431159309782129L;

    /**
     * 属性名
     */
    @ApiModelProperty("属性名")
    private String fieldName;

    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    private Object fieldValue;

    /**
     * 比较操作类型
     */
    @ApiModelProperty(value = "比较操作类型")
    private ComparatorOperator comparatorOperator = ComparatorOperator.EQUAL;

    /**
     * 条件关联类型
     */
    @ApiModelProperty("条件关联类型")
    private LogicalOperator logicalOperator = LogicalOperator.AND;

    public FieldObject(String fieldName, Object fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public FieldObject(String fieldName, Object fieldValue, JpaCriteriaHelper.ComparatorOperator comparatorOperator) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.comparatorOperator = comparatorOperator;
    }

    public FieldObject(String fieldName, Object fieldValue, ComparatorOperator comparatorOperator,
                       LogicalOperator logicalOperator) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.comparatorOperator = comparatorOperator;
        this.logicalOperator = logicalOperator;
    }
}
