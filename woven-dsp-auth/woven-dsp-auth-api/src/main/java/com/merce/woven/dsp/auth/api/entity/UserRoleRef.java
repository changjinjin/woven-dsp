package com.merce.woven.dsp.auth.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ref_user_role")
public class UserRoleRef implements Serializable {
    private static final long serialVersionUID = -4066909154102918575L;

    @Id
    @ApiModelProperty(value = "用户ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String userId;

    @Id
    @ApiModelProperty(value = "角色ID")
    @Column(length = 50, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String roleId;

    public UserRoleRef() {
    }

    public UserRoleRef(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

}
