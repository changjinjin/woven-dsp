package com.info.baymax.dsp.data.platform.entity;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.dsp.data.platform.bean.GrowthType;
import com.info.baymax.dsp.data.platform.bean.TransferType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;

/**
 * 数据传输记录
 *
 * @author jingwei.yang
 * @date 2020年4月28日 下午4:22:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_transfer_record", indexes = {@Index(columnList = "lastModifiedTime DESC")})
@Comment("数据传输记录")
public class DataTransferRecord extends BaseEntity {
    private static final long serialVersionUID = 3127150491057357093L;

    @ApiModelProperty("消费者ID")
    @Comment("消费者ID")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custId;

    @ApiModelProperty("消费者名称")
    @Comment("消费者名称")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custName;

    @ApiModelProperty("消费者应用ID")
    @Comment("消费者应用ID")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custAppId;

    @ApiModelProperty("消费者应用ID")
    @Comment("消费者应用ID")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String custAppName;

    @ApiModelProperty("数据服务ID")
    @Comment("数据服务ID")
    @Column(length = 20, nullable = false)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long dataServiceId;

    @ApiModelProperty("数据服务名称")
    @Comment("数据服务名称")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String dataServiceName;

    @ApiModelProperty("传输开始时间")
    @Comment("传输开始时间")
    @Column
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long startTime;

    @ApiModelProperty("传输结束时间")
    @Comment("传输结束时间")
    @Column
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long endTime;

    @ApiModelProperty("数据传输总耗时（毫秒数）")
    @Comment("数据传输总耗时（毫秒数）")
    @Column
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long cost;

    @ApiModelProperty(value = "服务启动类型: 0 pull, 1 push")
    @Comment("服务启动类型: 0 pull, 1 push")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("PULL")
    private TransferType transferType;

    @ApiModelProperty(value = "数据传输增长方式: 0 全量, 1 增量")
    @Comment("数据传输增长方式: 0 全量, 1 增量")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("TOTAL")
    private GrowthType growthType;

    @ApiModelProperty("当前服务中的增量值")
    @Comment("当前服务中的增量值")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String cursorVal;

    @ApiModelProperty(value = "数据传输起始位置")
    @Comment("数据传输起始位置")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long offset;

    @ApiModelProperty(value = "数据传输记录数")
    @Comment("数据传输记录数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long records;

    @ApiModelProperty(value = "数据传输结果状态码")
    @Comment("数据传输结果状态码")
    @Column(length = 3)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private Integer resultCode;

    public DataTransferRecord() {
    }

    public DataTransferRecord(String name, String tenantId, String owner, String custId, String custName,
                              Long custAppId, String custAppName, Long dataServiceId, String dataServiceName, Long startTime,
                              Long endTime, Long cost, TransferType transferType, GrowthType growthType, String cursorVal, Long offset,
                              Long records, Integer resultCode) {
        super();
        this.name = name;
        this.tenantId = tenantId;
        this.owner = owner;
        this.custId = custId;
        this.custName = custName;
        this.custAppId = custAppId;
        this.custAppName = custAppName;
        this.dataServiceId = dataServiceId;
        this.dataServiceName = dataServiceName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.transferType = transferType;
        this.growthType = growthType;
        this.cursorVal = cursorVal;
        this.offset = offset;
        this.records = records;
        this.resultCode = resultCode;
    }

    public static DataTransferRecord pull(String name, String tenantId, String owner, String custId, String custName,
                                          Long custAppId, String custAppName, Long dataServiceId, String dataServiceName, Long startTime,
                                          Long endTime, Long cost, GrowthType growthType, String cursorVal, Long offset, Long records,
                                          Integer resultCode) {
        return new DataTransferRecord(name, tenantId, owner, custId, custName, custAppId, custAppName, dataServiceId,
            dataServiceName, startTime, endTime, cost, TransferType.PULL, growthType, cursorVal, offset, records,
            resultCode);
    }

    public static DataTransferRecord push(String name, String tenantId, String owner, String custId, String custName,
                                          Long custAppId, String custAppName, Long dataServiceId, String dataServiceName, Long startTime,
                                          Long endTime, Long cost, GrowthType growthType, String cursorVal, Long offset, Long records,
                                          Integer resultCode) {
        return new DataTransferRecord(name, tenantId, owner, custId, custName, custAppId, custAppName, dataServiceId,
            dataServiceName, startTime, endTime, cost, TransferType.PUSH, growthType, cursorVal, offset, records,
            resultCode);
    }
}
