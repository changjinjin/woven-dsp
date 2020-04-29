package com.info.baymax.dsp.data.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.dsp.data.platform.bean.TransferType;
import com.info.baymax.dsp.data.platform.mybatis.type.TransferTypeEnumTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

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
@Table(name = "dsp_data_transfer_record", indexes = { @Index(columnList = "lastModifiedTime DESC") })
@Comment("数据传输记录")
public class DataTransferRecord extends BaseEntity {
	private static final long serialVersionUID = 3127150491057357093L;

	@ApiModelProperty("消费者ID")
	@Comment("消费者ID")
	@Column(length = 20, nullable = false)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private String custId;

	@ApiModelProperty("消费者应用accessKey")
	@Comment("消费者应用accessKey")
	@Column(length = 50, nullable = false)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String accessKey;

	@ApiModelProperty("数据服务ID")
	@Comment("数据服务ID")
	@Column(length = 20, nullable = false)
	@ColumnType(jdbcType = JdbcType.BIGINT)
	private String dataServiceId;

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
	@ColumnType(jdbcType = JdbcType.INTEGER, typeHandler = TransferTypeEnumTypeHandler.class)
	@ColumnDefault("0")
	private TransferType transferType;

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

	public DataTransferRecord(String name, String tenantId, String owner, String custId, String accessKey,
			String dataServiceId, Long startTime, Long endTime, Long cost, TransferType transferType, Long offset,
			Long records, Integer resultCode) {
		super();
		this.custId = custId;
		this.accessKey = accessKey;
		this.dataServiceId = dataServiceId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.cost = cost;
		this.transferType = transferType;
		this.offset = offset;
		this.records = records;
		this.resultCode = resultCode;
	}

	public static DataTransferRecord pull(String name, String tenantId, String owner, String custId, String accessKey,
			String dataServiceId, Long startTime, Long endTime, Long cost, Long offset, Long records,
			Integer resultCode) {
		return new DataTransferRecord(name, tenantId, owner, custId, accessKey, dataServiceId, startTime, endTime, cost,
				TransferType.PULL, offset, records, resultCode);
	}

	public static DataTransferRecord push(String name, String tenantId, String owner, String custId, String accessKey,
			String dataServiceId, Long startTime, Long endTime, Long cost, Long offset, Long records,
			Integer resultCode) {
		return new DataTransferRecord(name, tenantId, owner, custId, accessKey, dataServiceId, startTime, endTime, cost,
				TransferType.PULL, offset, records, resultCode);
	}
}
