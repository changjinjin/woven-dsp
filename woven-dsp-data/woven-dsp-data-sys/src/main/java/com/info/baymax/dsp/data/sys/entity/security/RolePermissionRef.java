package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.mybatis.type.bool.BooleanVsIntegerTypeHandler;
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
    protected Long roleId;

    @Id
    @ApiModelProperty(value = "权限ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected Long permissionId;

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.BOOLEAN, typeHandler = BooleanVsIntegerTypeHandler.class)
    @DefaultValue("false")
    protected Boolean halfSelect;

    public RolePermissionRef() {
    }

    public RolePermissionRef(Long roleId, Long permissionId, Boolean halfSelect) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.halfSelect = halfSelect;
    }

}
