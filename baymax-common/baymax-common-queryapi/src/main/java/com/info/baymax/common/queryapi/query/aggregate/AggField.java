package com.info.baymax.common.queryapi.query.aggregate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@Data
@EqualsAndHashCode
@Builder
public class AggField implements Serializable {
    private static final long serialVersionUID = -878572549536122362L;

    @ApiModelProperty(value = "聚合字段名称",required = true)
    private String name;

    @ApiModelProperty(value = "聚合后别名",required = false)
    private String alias;

    @ApiModelProperty(value = "聚合类型",required = true)
    private AggType aggType;

    @ApiModelProperty(value = "是否去重",required = false)
    private boolean distinct;

    public String getAlias() {
        if (alias == null || alias.isEmpty()) {
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
