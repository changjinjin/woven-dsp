package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.persistence.mybatis.type.bool.BooleanVsIntegerTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "ref_role_resource", indexes = {@Index(columnList = "roleId")})
@Comment("角色资源目录关联信息表")
public class RoleResourceRef implements Serializable {
    private static final long serialVersionUID = -4066909154102918575L;

    @Id
    @ApiModelProperty(value = "角色ID")
    @Comment("角色ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String roleId;

    @Id
    @ApiModelProperty(value = "资源目录ID")
    @Comment("资源目录ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    @ApiModelProperty("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    @Comment("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resType;

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Comment("是否是全选状态：false-否（半选中），true-是，默认0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.BOOLEAN, typeHandler = BooleanVsIntegerTypeHandler.class)
    @ColumnDefault("false")
    protected Boolean halfSelect;

    @ApiModelProperty("计数器")
    @Comment("计数器")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("1")
    private Integer counter;

    public RoleResourceRef() {
    }

    public RoleResourceRef(String roleId) {
        this.roleId = roleId;
    }

    public RoleResourceRef(String roleId, String resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public RoleResourceRef(String roleId, String resourceId, String resType, Boolean halfSelect) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.resType = resType;
        this.halfSelect = halfSelect;
    }

    public RoleResourceRef(String roleId, String resourceId, String resType, Boolean halfSelect, Integer counter) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.resType = resType;
        this.halfSelect = halfSelect;
        this.counter = counter;
    }

}
