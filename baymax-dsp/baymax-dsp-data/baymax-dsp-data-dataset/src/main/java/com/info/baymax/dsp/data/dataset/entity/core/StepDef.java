package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.persistence.entity.base.Maintable;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsConfigObjectTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.varchar.GZBase64VarcharVsStepFieldGroupTypeHandler;
import com.merce.woven.common.ConfigObject;
import com.merce.woven.common.StepFieldGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_step_definition")
@Comment("流程节点定义信息表")
public class StepDef extends Maintable {
    private static final long serialVersionUID = -8852321025466517186L;

    @ApiModelProperty("节点类型")
    @Comment("节点类型")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String type;

    @ApiModelProperty("前端配置信息")
    @Comment("前端配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsConfigObjectTypeHandler.class)
    protected ConfigObject uiConfigurations;

    @ApiModelProperty("额外配置信息")
    @Comment("额外配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsConfigObjectTypeHandler.class)
    protected ConfigObject otherConfigurations;

    @ApiModelProperty("输入配置信息")
    @Comment("输入配置信息")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsStepFieldGroupTypeHandler.class)
    protected StepFieldGroup inputConfigurations;

    @ApiModelProperty("输出配置信息")
    @Comment("输出配置信息")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsStepFieldGroupTypeHandler.class)
    protected StepFieldGroup outputConfigurations;

    @ApiModelProperty("所属分类")
    @Comment("所属分类")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
    protected List<String> tags;

    @ApiModelProperty("所属分组")
    @Comment("所属分组")
    @Column(name = "step_group", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String group;

    @ApiModelProperty("输入数量")
    @Comment("输入数量")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String inputCount;

    @ApiModelProperty("图标")
    @Comment("图标")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String icon = "default.png";

    @ApiModelProperty("库信息")
    @Comment("库信息")
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
    protected List<String> libs;

    @ApiModelProperty("继承信息")
    @Comment("继承信息")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String implementation;

    public StepDef() {
    }

    public StepDef(String type, String name) {
        this(type, name, new ArrayList<String>(), "", new ConfigObject(), new StepFieldGroup(), new StepFieldGroup());
    }

    public StepDef(String type, String name, List<String> libs, String implementation) {
        this(type, name, libs, implementation, new ConfigObject(), new StepFieldGroup(), new StepFieldGroup());
    }

    public StepDef(String type, String name, List<String> libs, String implementation, ConfigObject otherConfigurations,
                   StepFieldGroup inputConfigurations, StepFieldGroup outputConfigurations) {
        this(type, name, libs, implementation, otherConfigurations, inputConfigurations, outputConfigurations, null);
    }

    public StepDef(String type, String name, List<String> libs, String implementation, ConfigObject otherConfigurations,
                   StepFieldGroup inputConfigurations, StepFieldGroup outputConfigurations, String inputCount) {
        this.type = type;
        this.name = name;
        this.libs = libs;
        this.implementation = implementation;
        this.otherConfigurations = otherConfigurations;
        this.inputConfigurations = inputConfigurations;
        this.outputConfigurations = outputConfigurations;
        this.inputCount = inputCount;
    }
}
