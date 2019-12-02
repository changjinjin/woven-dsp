//package com.jusfoun.services.auth.api.entity;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Table;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//
//@ApiModel
//@Table(name = "sys_role_user")
//public class SysRoleUser implements Serializable {
//	private static final long serialVersionUID = -1570907147241596748L;
//
//	/**
//	 * 用户id
//	 */
//	@ApiModelProperty("用户id")
//	@Column(name = "user_id")
//	private Long userId;
//
//	/**
//	 * 角色id
//	 */
//	@ApiModelProperty("角色id")
//	@Column(name = "role_id")
//	private Long roleId;
//
//	public Long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//
//	public Long getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(Long roleId) {
//		this.roleId = roleId;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
//		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SysRoleUser other = (SysRoleUser) obj;
//		if (roleId == null) {
//			if (other.roleId != null)
//				return false;
//		} else if (!roleId.equals(other.roleId))
//			return false;
//		if (userId == null) {
//			if (other.userId != null)
//				return false;
//		} else if (!userId.equals(other.userId))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "SysRoleUser [userId=" + userId + ", roleId=" + roleId + "]";
//	}
//
//}