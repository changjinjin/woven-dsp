package com.info.baymax.dsp.data.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 数据资源实体类，该记录在用户将baymax的数据集关联到dsp后生成，消费者可以对其申请，管理员可以对其审批
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "data_resource")
public class DataResource implements Serializable {
    /*
    1. 数据资源关联:  与baymax数据进行关联,  管理员登录 "管理平台", 进入"数据管理"->"数据设置", 选择数据集(现有baymax中数据集), 点击 "关联", 弹出配置窗口(或页面)
   - 数据类型 (结构, 半结构, 非结构)
   - 设置 数据标签,便于识别, 默认使用name
   - 设置 数据支持功能(增量触发, 增量, 全量, 分页, 排序, 单条), 设置增量字段, 增量字段类型, 由数据存储类型自动筛选出可配置项
   - 设置 数据所在分类(目录)
   - 核对 数据编码
   - 核对 数据信息, 例如 有效期(精确到日) , 数据字段定义 等
   - 点击 "确认",完成数据关联设置
   * 完成后, 此时数据在 "数据管理"->"数据资源" 中可见(此功能为过渡功能)*/
    //id, name, label, type, dataset_id, engine, encoder, configuration, expired_time, tenant_id, creator, modifier, create_time, last_modified_time

    @Id
    @ApiModelProperty(value = "数据资源ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long id;

    @ApiModelProperty(value = "资源名称")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String name;

    @ApiModelProperty(value = "标签别名")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String label;

    @ApiModelProperty(value = "数据类型: 0 structured, 1 semi-structured, 2 unstructured")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("0")
    private Integer type;


    @ApiModelProperty(value = "关联的baymax系统中数据集ID")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String datasetId;

    @ApiModelProperty(value = "数据集存储引擎")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String engine;

    @ApiModelProperty(value = "数据集对应的编码")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String encoder;

    @ApiModelProperty("存储原有Dataset配置参数,包含schema信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> datasetConfiguration;

    @ApiModelProperty("数据资源的配置参数,消费者配置一些规则包括开发类型,服务方式,开放字段,共享策略等")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> storageConfiguration;

    @ApiModelProperty("过期时间")
    @JsonIgnore
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long expiredTime;// = 0L;

    @ApiModelProperty("所属租户ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long tenantId;

    @ApiModelProperty("创建人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("SYSTEM")
    private String creator;// = "SYSTEM";

    @ApiModelProperty("修改人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("SYSTEM")
    private String lastModifier;// = "SYSTEM";

    @ApiModelProperty("创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    private Date createTime; // = new Date();

    @ApiModelProperty("修改时间")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime = new Date();

    public DataResource() {
    }


}
