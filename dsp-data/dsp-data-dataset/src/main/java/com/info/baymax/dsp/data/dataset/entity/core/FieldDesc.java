package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldDesc implements java.io.Serializable {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("别名")
    private String alias;

    @ApiModelProperty("备注")
    private String description;

    /**
     * 非原生字段标识。
     * 默认值是false，表示原生字段，序列化与反序列化会过滤掉值是false的nonNativeColumn属性
     */
    @ApiModelProperty("非原生字段")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean nonNativeColumn = false;

    public FieldDesc() {
    }

    public FieldDesc(String name) {
        this.name = name;
    }

    public FieldDesc(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public FieldDesc(String name, String type, String alias) {
        this.name = name;
        this.type = type;
        this.alias = alias;
    }

    public FieldDesc(String name, String type, String alias, boolean nonNativeColumn) {
        this.name = name;
        this.type = type;
        this.alias = alias;
        this.nonNativeColumn = nonNativeColumn;
    }

    public FieldDesc(String name, String type, String alias, String description) {
        this.name = name;
        this.type = type;
        this.alias = alias;
        this.description = description;
    }

    public FieldDesc(String name, String type, String alias, String description, boolean nonNativeColumn) {
        this.name = name;
        this.type = type;
        this.alias = alias;
        this.description = description;
        this.nonNativeColumn = nonNativeColumn;
    }

    public boolean changed() {
        return !name.equals(alias);
    }
}
