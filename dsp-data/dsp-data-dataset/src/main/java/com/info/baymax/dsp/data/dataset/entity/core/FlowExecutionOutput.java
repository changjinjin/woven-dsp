package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.entity.base.Maintable;
import org.hibernate.annotations.ColumnDefault;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_flow_execution_output", indexes = {@Index(columnList = "executionId"),
    @Index(columnList = "flowId"), @Index(columnList = "lastModifiedTime DESC")})
@Comment("流程执行输出信息表")
public class FlowExecutionOutput extends Maintable {
    private static final long serialVersionUID = 998982341570779840L;

    @ApiModelProperty("输出字节数")
    @Comment("输出字节数")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long outputSize;

    @ApiModelProperty("输出数据记录数")
    @Comment("输出数据记录数")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long outputRecords;

    @ApiModelProperty("操作类型")
    @Comment("操作类型")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String action;

    @ApiModelProperty("流程执行ID")
    @Comment("流程执行ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String executionId;

    @ApiModelProperty("流程ID")
    @Comment("流程ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String flowId;

    @ApiModelProperty("执行步骤相对路径")
    @Comment("执行步骤相对路径")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String stepPath;

    @ApiModelProperty("元数据ID")
    @Comment("元数据ID")
    @Column(name = "schema_id", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String schema;

    @ApiModelProperty("元数据名称")
    @Comment("元数据名称")
    @Transient
    private String schemaName;

    @ApiModelProperty("数据集信息")
    @Comment("数据集信息")
    @JsonIgnore
    @Lob
    @ColumnType(jdbcType = JdbcType.CLOB)
    private String datasetJson;

    public FlowExecutionOutput() {
        super();
    }

    public FlowExecutionOutput(long outputSize, long outputRecords, String action, String executionId, String flowId,
                               String stepPath, String schema, String datasetJson) {
        super();
        this.outputSize = outputSize;
        this.outputRecords = outputRecords;
        this.action = action;
        this.executionId = executionId;
        this.flowId = flowId;
        this.stepPath = stepPath;
        this.schema = schema;
        this.datasetJson = datasetJson;
    }

    @Transient
    public Dataset getDataset() {
        try {
            if (datasetJson == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(datasetJson, Dataset.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDataset(Dataset dataset) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            datasetJson = mapper.writeValueAsString(dataset);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        schema = dataset.getSchemaId();
    }
}
