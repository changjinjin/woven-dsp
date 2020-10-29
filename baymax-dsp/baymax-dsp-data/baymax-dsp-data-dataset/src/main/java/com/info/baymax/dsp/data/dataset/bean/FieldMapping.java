package com.info.baymax.dsp.data.dataset.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@ApiModel
@NoArgsConstructor
@Data
public class FieldMapping implements Comparable<FieldMapping>, Serializable {

    private static final long serialVersionUID = -8980720618228630927L;

    @ApiModelProperty("索引")
    private int index;

    @ApiModelProperty("源字段")
    private String sourceField;

    @ApiModelProperty("源字段类型")
    private String sourceType;

    @ApiModelProperty("目标字段")
    private String targetField;

    @ApiModelProperty("目标字段类型")
    private String targetType;

    @ApiModelProperty("解密类型")
    private String encrypt; // BLANK, MIX

    @ApiModelProperty("转换规则")
    private TransformRule transformRule;
    
    @ApiModelProperty("支持的聚合函数列表，如：count，avg，sum，max，min")
    private Set<String> supportAggs;

    public FieldMapping(String sourceField, String targetField) {
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    public FieldMapping(String sourceField, String sourceType, String targetField, String targetType) {
        this.sourceField = sourceField;
        this.sourceType = sourceType;
        this.targetField = targetField;
        this.targetType = targetType;
    }

    @Override
    public int compareTo(FieldMapping o) {
        return this.index > o.index ? 1 : -1;
    }
}
