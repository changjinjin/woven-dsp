package com.merce.woven.dsp.auth.api.entity;

import com.merce.woven.common.entity.field.DefaultValue;
import com.merce.woven.common.mybatis.type.bool.BooleanVsIntegerTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "ref_role_permission")
public class RolePermissionRef implements Serializable {
    private static final long serialVersionUID = -4066909154102918575L;

    @Id
    @ApiModelProperty(value = "角色ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String roleId;

    @Id
    @ApiModelProperty(value = "权限ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String permissionId;

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.BOOLEAN, typeHandler = BooleanVsIntegerTypeHandler.class)
    @DefaultValue("false")
    protected Boolean halfSelect;

    public RolePermissionRef() {
    }

    public RolePermissionRef(String roleId, String permissionId, Boolean halfSelect) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.halfSelect = halfSelect;
    }

}
