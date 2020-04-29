package com.info.baymax.dsp.data.sys.entity.security;

import com.google.common.collect.Lists;
import com.info.baymax.common.entity.base.Maintable;
import org.hibernate.annotations.ColumnDefault;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;

import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})})
@Comment("角色信息表")
public class Role extends Maintable {
    private static final long serialVersionUID = -4302027743711660884L;

    @XmlElement(name = "客户端ID")
    @ApiModelProperty(value = "客户端ID")
    @Comment("客户端ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'baymax'")
    private String clientId;

    @ApiModelProperty("角色权限列表")
    @Transient
    private Set<Permission> permissions;

    @ApiModelProperty(value = "权限ID列表")
    @Transient
    private Set<String> permIds;

    @ApiModelProperty(value = "角色资源关系组")
    @Transient
    private List<RoleResourceRefGroup> rrrfGroups = Lists.newArrayList();

    public Set<String> getPermIds() {
        if (ICollections.hasElements(permissions)) {
            permIds = permissions.stream().map(t -> t.getId()).collect(Collectors.toSet());
        }
        return permIds;
    }
}
