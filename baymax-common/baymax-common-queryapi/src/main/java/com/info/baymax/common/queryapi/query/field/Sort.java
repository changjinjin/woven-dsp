package com.info.baymax.common.queryapi.query.field;

import java.io.Serializable;

import com.info.baymax.common.queryapi.query.field.SqlEnums.OrderType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 排序属性包装对象
 *
 * @author jingwei.yang
 * @date 2019年5月5日 下午12:10:29
 */
@ApiModel
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sort implements Serializable {
    private static final long serialVersionUID = -8413317033449069620L;

    /**
     * 排序的属性名称
     */
    @ApiModelProperty("排序的属性名称")
    private String name;

    /**
     * 排序类型：ASC或DESC，默认ASC
     */
    @ApiModelProperty("排序类型：ASC或DESC，默认ASC")
    private OrderType order = OrderType.ASC;

    public Sort(String fieldName) {
        this(fieldName, OrderType.ASC);
    }

    public static Sort apply(String fieldName) {
        return new Sort(fieldName);
    }

    public static Sort apply(String fieldName, OrderType orderDirection) {
        return new Sort(fieldName, orderDirection);
    }
}
