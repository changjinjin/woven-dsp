package com.info.baymax.dsp.data.consumer.entity;

import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyObjectValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_cust_data_source", uniqueConstraints = {@UniqueConstraint(columnNames = {"owner", "name"})})
@Comment("消费者数据源信息表")
public class CustDataSource extends BaseEntity {
    private static final long serialVersionUID = -4792968324286483492L;

    @ApiModelProperty(value = "推送数据源类型")
    @Comment("推送数据源类型")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String type;

    @ApiModelProperty(value = "推送数据源配置")
    @Comment("推送数据源配置")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyObjectValueTypeHandler.class)
    private Map<String, Object> attributes;

}
