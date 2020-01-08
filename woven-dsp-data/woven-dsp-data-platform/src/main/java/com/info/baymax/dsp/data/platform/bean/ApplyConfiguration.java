package com.info.baymax.dsp.data.platform.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @Author: haijun
 * @Date: 2019/12/31 19:16
 */
@Data
public class ApplyConfiguration implements Serializable {
    private static final long serialVersionUID = 2172351208078908585L;

    @ApiModelProperty(value = "数据资源ID")
    private Long dataResId;

    @ApiModelProperty(value = "数据资源名称")
    private String dataResName;

    @ApiModelProperty("PUSH操作相关配置,关联CustDataResource获取push信息")
    private Long custDataSourceId;

    @ApiModelProperty("数据源名称")
    private String custDataSourceName;

    @ApiModelProperty("数据源表名称")
    private String custTableName;

    @ApiModelProperty("PULL操作配置,关联CustApp获取接入配置信息")
    private Long custAppId;

    @ApiModelProperty(value = "服务方式,pull和push不同,全量,增量,列表")
    private Integer serviceMode;
}
