package com.info.baymax.dsp.data.sys.entity.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;

import org.hibernate.annotations.ColumnDefault;
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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "ref_role_permission")
@Comment("角色权限关联信息表")
public class RolePermissionRef implements Serializable {
    private static final long serialVersionUID = -4066909154102918575L;

    @Id
    @ApiModelProperty(value = "角色ID")
    @Comment("角色ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String roleId;

    @Id
    @ApiModelProperty(value = "权限ID")
    @Comment("权限ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String permissionId;

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Comment("是否是全选状态：false-否（半选中），true-是，默认0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.BOOLEAN, typeHandler = BooleanVsIntegerTypeHandler.class)
    @ColumnDefault("false")
    protected Boolean halfSelect;

    public RolePermissionRef() {
    }

    public RolePermissionRef(String roleId, String permissionId, Boolean halfSelect) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.halfSelect = halfSelect;
    }

}
