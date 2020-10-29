package com.info.baymax.data.elasticsearch.entity;

import io.searchbox.annotations.JestId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据传输记录
 *
 * @author jingwei.yang
 * @date 2020年4月28日 下午4:22:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class DataTransferRecord implements Serializable {
    private static final long serialVersionUID = 3127150491057357093L;
    public static String TYPE_NAME = "dataset_access_record";

    @ApiModelProperty("主键")
    @JestId
    private String id;

    @ApiModelProperty("sid")
    private String sid;

    @ApiModelProperty("写入时间")
    private Date write_time;

    @ApiModelProperty("数据集名称")
    private String datasetId;

    @ApiModelProperty("数据集名称")
    private String datasetName;

    @ApiModelProperty("消费者ID")
    private String custId;

    @ApiModelProperty("消费者名称")
    private String custName;

    @ApiModelProperty("消费者应用ID")
    private Long custAppId;

    @ApiModelProperty("消费者应用ID")
    private String custAppName;

    @ApiModelProperty("数据服务ID")
    private Long dataServiceId;

    @ApiModelProperty("数据服务名称")
    private String dataServiceName;

    @ApiModelProperty("传输开始时间")
    private Long startTime;

    @ApiModelProperty("传输结束时间")
    private Long endTime;

    @ApiModelProperty("数据传输总耗时（毫秒数）")
    private Long cost;

    @ApiModelProperty(value = "服务启动类型: 0 pull, 1 push")
    private String transferType;

    @ApiModelProperty(value = "数据传输增长方式: 0 全量, 1 增量")
    private String growthType;

    @ApiModelProperty("当前服务中的增量值")
    private String cursorVal;

    @ApiModelProperty(value = "数据传输起始位置")
    private Long offset;

    @ApiModelProperty(value = "数据传输记录数")
    private Long records;

    @ApiModelProperty(value = "数据传输结果状态码")
    private Integer resultCode;

    @ApiModelProperty("租户ID")
    private String tenantId;

    @ApiModelProperty("所属人ID")
    private String owner;
}
