package com.info.baymax.dsp.data.platform.entity;

import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.clob.ClobVsMapStringKeyStringValueTypeHandler;
import com.info.baymax.common.persistence.mybatis.type.varchar.VarcharVsIntegerArrayTypeHandler;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.dsp.data.dataset.bean.FieldMapping;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListFieldMappingTypeHandler;
import com.info.baymax.dsp.data.platform.mybatis.type.SourceTypeTypeHandler;
import com.info.baymax.dsp.data.platform.mybatis.type.SqlQueryTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/13 18:55 数据资源实体类，该记录在用户将baymax的数据集关联到dsp后生成，消费者可以对其申请，管理员可以对其审批
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_resource", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {@Index(columnList = "tenantId,storage"),
    @Index(columnList = "lastModifiedTime DESC")})
@Comment("数据资源信息表")
public class DataResource extends BaseEntity {
    private static final long serialVersionUID = -1646060649387068719L;

    // 9999-12-31
    protected static Long MAX_DATE_TIME = 253402214400L;

    /*
     * 1. 数据资源关联: 与baymax数据进行关联, 管理员登录 "管理平台", 进入"数据管理"->"数据设置", 选择数据集(现有baymax中数据集), 点击 "关联", 弹出配置窗口(或页面) - 数据类型 (结构,
     * 半结构, 非结构) - 设置 数据标签,便于识别, 默认使用name - 设置 数据支持功能(增量触发, 增量, 全量, 分页, 排序, 单条), 设置增量字段, 增量字段类型, 由数据存储类型自动筛选出可配置项 - 设置
     * 数据所在分类(目录) - 核对 数据编码 - 核对 数据信息, 例如 有效期(精确到日) , 数据字段定义 等 - 点击 "确认",完成数据关联设置 完成后, 此时数据在 "数据管理"->"数据资源"
     * 中可见(此功能为过渡功能)
     */
    @ApiModelProperty(value = "是否支持pull:0不支持,1支持")
    @Comment("是否支持pull:0不支持,1支持")
    @Column(length = 1)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer isPull;

    @ApiModelProperty(value = "是否支持push:0不支持,1支持")
    @Comment("是否支持push:0不支持,1支持")
    @Column(length = 1)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer isPush;

    @ApiModelProperty(value = "pull服务方式,2 列表")
    @Comment("pull服务方式,2 列表")
    @Convert(converter = ObjectToStringConverter.class)
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsIntegerArrayTypeHandler.class)
    private Integer[] pullServiceMode;

    @ApiModelProperty(value = "push服务方式:0 全量,1 增量")
    @Comment("push服务方式:0 全量,1 增量")
    @Convert(converter = ObjectToStringConverter.class)
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsIntegerArrayTypeHandler.class)
    private Integer[] pushServiceMode;

    @ApiModelProperty(value = "数据类型: 0 structured, 1 semi-structured, 2 unstructured")
    @Comment("数据类型: 0 structured, 1 semi-structured, 2 unstructured")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer type;

    @ApiModelProperty(value = "关联的baymax系统中数据集ID")
    @Comment("关联的baymax系统中数据集ID")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String datasetId;

    @ApiModelProperty(value = "数据集名称")
    @Comment("数据集名称")
    @Column(length = 255, nullable = true)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String datasetName;

    @ApiModelProperty(value = "增量字段,可以为空,增量字段只有一个并且只支持整型和时间戳类型")
    @Comment("增量字段,可以为空,增量字段只有一个并且只支持整型和时间戳类型")
    @Column(length = 255, nullable = true)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String incrementField;

    @ApiModelProperty(value = "数据集存储引擎")
    @Comment("数据集存储引擎")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String storage;

    @ApiModelProperty(value = "存储目录(分类)ID")
    @Comment("存储目录(分类)ID")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long categoryId;

    @ApiModelProperty(value = "数据集对应的编码,默认utf8")
    @Comment("数据集对应的编码,默认utf8")
    @Column(length = 255, nullable = true)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'utf8'")
    private String encoder;

    @ApiModelProperty("开放字段及配置字段映射关系")
    @Comment("开放字段及配置字段映射关系")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListFieldMappingTypeHandler.class)
    private List<FieldMapping> fieldMappings;

    @ApiModelProperty("管理员在关联数据集时进行的一些基本配置")
    @Comment("管理员在关联数据集时进行的一些基本配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> baseConfiguration;

    @ApiModelProperty("数据发布配置信息")
    @Comment("数据发布配置信息")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> publishConfiguration;

    @ApiModelProperty(value = "开放状态: 0 未开放, 1 已开放,2 已到期")
    @Comment("开放状态: 0 未开放, 1 已开放,2 已到期")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer openStatus;

    @ApiModelProperty("此数据资源的有效期,服务期限")
    @Comment("此数据资源的有效期,服务期限")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long expiredTime;

    @ApiModelProperty("访问次数")
    @Comment("访问次数")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer accessTimes;

    @ApiModelProperty("数据来源")
    @Comment("数据来源")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'Baymax'")
    private String source;

    @ApiModelProperty("数据类型:DATASET, DATASOURCE")
    @Comment("数据类型:DATASET, DATASOURCE")
    @Enumerated(EnumType.STRING)
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = SourceTypeTypeHandler.class)
    @ColumnDefault("'DATASET'")
    private SourceType sourceType;

    @ApiModelProperty("sql查询配置信息")
    @Comment("sql查询配置信息")
    @Convert(converter = ObjectToStringConverter.class)
    @Column(length = 4000)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = SqlQueryTypeHandler.class)
    private SqlQuery query;
}