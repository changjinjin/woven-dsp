package com.info.baymax.common.queryapi.aggregate;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel
@Data
@EqualsAndHashCode
@Builder
public class AggField implements Serializable {
    private static final long serialVersionUID = -878572549536122362L;

    @ApiModelProperty("聚合字段名称")
    private String name;

    @ApiModelProperty("聚合后别名")
    private String alias;

    @ApiModelProperty("聚合类型")
    private AggType aggType;

    @ApiModelProperty("是否去重")
    private boolean distinct;

    public String getAlias() {
        if (StringUtils.isEmpty(alias)) {
            this.alias = aggType.getValue() + "_" + name;
        }
        return this.alias;
    }

    public String getExpr() {
        return new StringBuffer().append(aggType.getValue()).append("(").append((distinct ? "distinct " : ""))
            .append(name).append(") ").append(getAlias()).toString();
    }

    public String getAggExpr() {
        return new StringBuffer().append(aggType.getValue()).append("(").append((distinct ? "distinct " : ""))
            .append(name).append(")").toString();
    }

    @Override
    public String toString() {
        return getExpr();
    }

    public static void main(String[] args) {
        AggField field = AggField.builder().name("age").aggType(AggType.AVG).distinct(false).build();
        System.err.println(field.toString());
    }

}
