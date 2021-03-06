package com.info.baymax.dsp.data.platform.entity;

import com.info.baymax.common.persistence.entity.base.BaseEntity;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.clob.ClobVsMapStringKeyStringValueTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Map;

/**
 * @Author: haijun
 * @Date: 2019/12/13 18:55
 * 数据服务的策略配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_policy")
@Comment("数据服务的策略配置信息表")
public class DataPolicy extends BaseEntity {

    private static final long serialVersionUID = -2450974106259820957L;

    @ApiModelProperty(value = "服务类型: 0 pull, 1 push")
    @Comment("服务类型: 0 pull, 1 push")
    @Column(length = 11, nullable = false)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer serviceType;

    @ApiModelProperty(value = "服务方式:file，message，http等")
    @Comment("服务方式:file，message，http等")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String serviceMethod;

    @ApiModelProperty("开放字段及配置字段映射关系,不加密")
    @Comment("开放字段及配置字段映射关系,不加密")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String,String> fieldMappings;

    @ApiModelProperty("特殊字段配置,如:加密,脱敏,转换等")
    @Comment("特殊字段配置,如:加密,脱敏,转换等")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String,Map<String,String>> transformMappings;

    @ApiModelProperty("此数据服务的有效期")
    @Comment("此数据服务的有效期")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long serviceExpiredTime;

    // 9999-12-31
    protected static Long MAX_DATE_TIME = 253402214400L;


}
