package com.info.baymax.dsp.data.sys.entity.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.ibatis.type.JdbcType;

import com.google.common.collect.Lists;
import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.utils.ICollections;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_sys_role", uniqueConstraints = { @UniqueConstraint(columnNames = { "tenantId", "name" }) })
public class Role extends BaseEntity {
	private static final long serialVersionUID = -4302027743711660884L;

	@ApiModelProperty("角色权限列表")
	@Transient
	private Set<Permission> permissions;

	@ApiModelProperty(value = "权限ID列表")
	@Transient
	private Set<Long> permIds;

	@ApiModelProperty(value = "角色资源关系组")
	@Transient
	private List<RoleResourceRefGroup> rrrfGroups = Lists.newArrayList();

	public Set<Long> getPermIds() {
		if (ICollections.hasElements(permissions)) {
			permIds = permissions.stream().map(t -> t.getId()).collect(Collectors.toSet());
		}
		return permIds;
	}
}
