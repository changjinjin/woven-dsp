package com.info.baymax.dsp.data.consumer.entity;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyObjectValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_cust_data_source", uniqueConstraints = {@UniqueConstraint(columnNames = {"custId", "name"})})
public class CustDataSource extends BaseEntity {
    private static final long serialVersionUID = -4792968324286483492L;

    @ApiModelProperty(value = "消费者ID")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    private Long custId;

    @ApiModelProperty(value = "推送数据源类型")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String type;

    @ApiModelProperty(value = "推送数据源配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyObjectValueTypeHandler.class)
    private Map<String, Object> attributes;

}
