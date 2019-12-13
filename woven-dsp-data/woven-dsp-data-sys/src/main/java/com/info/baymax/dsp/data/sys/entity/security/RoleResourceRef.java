package com.info.baymax.dsp.data.sys.entity.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.mybatis.type.bool.BooleanVsIntegerTypeHandler;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "ref_role_resource", indexes = {@Index(columnList = "roleId")})
public class RoleResourceRef implements Serializable {
    private static final long serialVersionUID = -4066909154102918575L;

    @Id
    @ApiModelProperty(value = "角色ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long roleId;

    @Id
    @ApiModelProperty(value = "资源目录ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private Long resourceId;

    @ApiModelProperty("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resType;

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.BOOLEAN, typeHandler = BooleanVsIntegerTypeHandler.class)
    @DefaultValue("false")
    protected Boolean halfSelect;

    @ApiModelProperty("计数器")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    private Integer counter;

    public RoleResourceRef() {
    }

    public RoleResourceRef(Long roleId) {
        this.roleId = roleId;
    }

    public RoleResourceRef(Long roleId, Long resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public RoleResourceRef(Long roleId, Long resourceId, String resType, Boolean halfSelect) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.resType = resType;
        this.halfSelect = halfSelect;
    }

    public RoleResourceRef(Long roleId, Long resourceId, String resType, Boolean halfSelect, Integer counter) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.resType = resType;
        this.halfSelect = halfSelect;
        this.counter = counter;
    }

}
