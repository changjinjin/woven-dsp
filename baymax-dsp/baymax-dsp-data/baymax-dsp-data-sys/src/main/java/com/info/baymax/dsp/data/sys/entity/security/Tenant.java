package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.entity.base.Maintable;
import org.hibernate.annotations.ColumnDefault;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;

import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_tenant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})}, indexes = {
    @Index(columnList = "lastModifiedTime")})
@Comment("租户信息表")
public class Tenant extends Maintable {
    private static final long serialVersionUID = -7861087791631568673L;

    @ApiModelProperty(value = "资源队列", required = true)
    @Comment("资源队列")
    @Lob
    @Column(length = 255)
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = GZBase64VarcharVsListStringTypeHandler.class)
    protected List<String> resourceQueues;

    @ApiModelProperty(value = "HDFS空间限额", required = false)
    @Comment("HDFS空间限额")
    @Column(length = 18)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    private Long hdfsSpaceQuota;

    @ApiModelProperty(value = "全局ID", required = false)
    @Comment("全局ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String zid;

    public static Tenant apply(String name, List<String> resourceQueues) {
        return new Tenant(name, resourceQueues);
    }

    public Tenant() {
    }

    public Tenant(String name) {
        this.name = name;
    }

    public Tenant(String name, List<String> resourceQueues) {
        this.name = name;
        this.resourceQueues = resourceQueues;
    }

}
