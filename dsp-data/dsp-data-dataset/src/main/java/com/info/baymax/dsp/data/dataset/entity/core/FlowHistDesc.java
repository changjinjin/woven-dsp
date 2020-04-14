package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListLinkDescTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListParameterDescTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListStepDescTypeHandler;
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
@JsonPropertyOrder({"id", "name", "description", "flowType", "source", "steps", "links", "paramters", "inputs",
    "outputs", "dependencies", "oid", "sharedUsers", "creator", "createTime", "lastModifier", "lastModifiedTime",
    "owner", "version", "enabled"})
@Entity
@Table(name = "merce_flow_history", indexes = {@Index(columnList = "o_id"), @Index(columnList = "version"),
    @Index(columnList = "lastModifiedTime")})
@Comment("流程历史信息表")
public class FlowHistDesc extends Maintable implements CryptoBean {
    private static final long serialVersionUID = -9153917488584209910L;

    @ApiModelProperty("来源")
    @Comment("来源")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String source;

    @ApiModelProperty("步骤节点信息")
    @Comment("步骤节点信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListStepDescTypeHandler.class)
    private List<StepDesc> steps;

    @ApiModelProperty("连线信息")
    @Comment("连线信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListLinkDescTypeHandler.class)
    private List<LinkDesc> links;

    @ApiModelProperty("流程类型：dataflow, workflow, streamflow")
    @Comment("流程类型：dataflow, workflow, streamflow")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowType;

    @ApiModelProperty("参数信息")
    @Comment("参数信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> parameters;

    @ApiModelProperty("输入信息")
    @Comment("输入信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> inputs;

    @ApiModelProperty("输出信息")
    @Comment("输出信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> outputs;

    @ApiModelProperty("依赖信息")
    @Comment("依赖信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> dependencies;

    @ApiModelProperty("原ID")
    @Comment("原ID")
    @Column(name = "o_id", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("$null")
    private String oid;

    @ApiModelProperty("所属项目信息")
    @Comment("所属项目信息")
    @Transient
    private ProjectEntity projectEntity;

    public FlowHistDesc() {
    }

    public FlowHistDesc(String name, String flowType, String oid, List<StepDesc> steps, List<LinkDesc> links) {
        this(name, "", flowType, oid, steps, links, null);
    }

    public FlowHistDesc(String name, String source, String flowType, String oid, List<StepDesc> steps,
                        List<LinkDesc> links) {
        this(name, source, flowType, oid, steps, links, null);
    }

    public FlowHistDesc(String name, String flowType, String oid, List<StepDesc> steps, List<LinkDesc> links,
                        List<ParameterDesc> parameters) {
        this(name, "", flowType, oid, steps, links, parameters);
    }

    public FlowHistDesc(String name, String source, String flowType, String oid, List<StepDesc> steps,
                        List<LinkDesc> links, List<ParameterDesc> parameters) {
        this.name = name;
        this.source = source;
        this.flowType = flowType;
        this.oid = oid;
        this.steps = steps;
        this.links = links;

        if (parameters == null) {
            parameters = new ArrayList<ParameterDesc>();
        }
        this.parameters = parameters;
    }

    @Override
    public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (steps != null && !steps.isEmpty()) {
            for (StepDesc step : steps) {
                step.encrypt(cryptoType, cryptorDelegater);
            }
        }
    }

    @Override
    public void decrypt(CryptorDelegater cryptorDelegater) {
        if (steps != null && !steps.isEmpty()) {
            for (StepDesc step : steps) {
                step.decrypt(cryptorDelegater);
            }
        }
    }
}
