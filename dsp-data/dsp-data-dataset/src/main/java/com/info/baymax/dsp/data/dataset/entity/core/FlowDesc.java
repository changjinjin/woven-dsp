package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.entity.base.Maintable;
import org.hibernate.annotations.ColumnDefault;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListLinkDescTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListParameterDescTypeHandler;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListStepDescTypeHandler;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import com.info.baymax.dsp.data.dataset.utils.ValueBind;
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
@Table(name = "merce_flow", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "tenantId,resourceId"), @Index(columnList = "lastModifiedTime DESC")})
@Comment("流程信息表")
public class FlowDesc extends Maintable implements ResourceId, CryptoBean {
    private static final long serialVersionUID = 4346804653240221818L;
    @ApiModelProperty("资源目录ID")
    @Comment("资源目录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    @ApiModelProperty("来源：input,output")
    @Comment("来源：input,output")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String source;

    @ApiModelProperty("步骤节点信息")
    @Comment("步骤节点信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListStepDescTypeHandler.class)
    private List<StepDesc> steps;

    @ApiModelProperty("节点连线信息")
    @Comment("节点连线信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListLinkDescTypeHandler.class)
    private List<LinkDesc> links;

    @ApiModelProperty("流程类型：dataflow, workflow, streamflow")
    @Comment("流程类型：dataflow, workflow, streamflow")
    @ValueBind(FieldType = ValueBind.fieldType.STRING, EnumStringValues = {"dataflow", "workflow", "streamflow"})
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowType;

    /**
     * 是否隐藏,对于qa flow不需要展示在Flow目录下，1 隐藏，0 显示，默认值为0
     */
    @ApiModelProperty("是否隐藏,对于qa flow不需要展示在Flow目录下，1 隐藏，0 显示，默认值为0")
    @Comment("是否隐藏,对于qa flow不需要展示在Flow目录下，1 隐藏，0 显示，默认值为0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer isHide;

    @ApiModelProperty("参数列表")
    @Comment("参数列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> parameters;

    @ApiModelProperty("输入参数列表")
    @Comment("输入参数列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> inputs;

    @ApiModelProperty("输出参数列表")
    @Comment("输出参数列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> outputs;

    @ApiModelProperty("依赖参数列表")
    @Comment("依赖参数列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListParameterDescTypeHandler.class)
    private List<ParameterDesc> dependencies;

    @ApiModelProperty("原ID")
    @Comment("原ID")
    @Column(name = "o_id", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("$null")
    private String oid;

    @ApiModelProperty("所属项目信息")
    @Comment("所属项目信息")
    @Transient
    private ProjectEntity projectEntity;

    @ApiModelProperty("所属资源目录信息")
    @Transient
    private ResourceDesc resource;

    public FlowDesc() {
    }

    public FlowDesc(String name, String flowType, String oid, List<StepDesc> steps, List<LinkDesc> links) {
        this(name, "", flowType, oid, steps, links, null);
    }

    public FlowDesc(String name, String flowType, String oid, List<StepDesc> steps, List<LinkDesc> links,
                    List<ParameterDesc> parameters) {
        this(name, "", flowType, oid, steps, links, parameters);
    }

    public FlowDesc(String name, String source, String flowType, String oid, List<StepDesc> steps, List<LinkDesc> links,
                    List<ParameterDesc> parameters) {
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

    // 获取resourceId,如果为空则从resource中获取
    public String getResourceId() {
        if (resourceId == null && resource != null) {
            resourceId = resource.getId();
        }
        return resourceId;
    }

    @Override
    public void encrypt(String secretKey, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (steps != null && !steps.isEmpty()) {
            for (StepDesc step : steps) {
                step.encrypt(secretKey, cryptoType, cryptorDelegater);
            }
        }
    }

    @Override
    public void decrypt(String secretKey, CryptorDelegater cryptorDelegater) {
        if (steps != null && !steps.isEmpty()) {
            for (StepDesc step : steps) {
                step.decrypt(secretKey, cryptorDelegater);
            }
        }
    }
}
