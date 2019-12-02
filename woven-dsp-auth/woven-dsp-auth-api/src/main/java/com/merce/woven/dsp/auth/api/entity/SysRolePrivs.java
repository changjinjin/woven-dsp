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
///**
// * 说明：角色权限关联模型. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2018年1月5日 上午9:10:21
// */
//@ApiModel
//@Table(name = "sys_role_privs")
//public class SysRolePrivs implements Serializable {
//	private static final long serialVersionUID = -8437861697252568978L;
//
//	@ApiModelProperty("角色ID")
//	@Column(name = "role_id")
//	private Long roleId;
//
//	@ApiModelProperty("模块ID")
//	@Column(name = "privs_id")
//	private Long privsId;
//
//	public Long getRoleId() {
//		return roleId;
//	}
//
//	public void setRoleId(Long roleId) {
//		this.roleId = roleId;
//	}
//
//	public Long getPrivsId() {
//		return privsId;
//	}
//
//	public void setPrivsId(Long privsId) {
//		this.privsId = privsId;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((privsId == null) ? 0 : privsId.hashCode());
//		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
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
//		SysRolePrivs other = (SysRolePrivs) obj;
//		if (privsId == null) {
//			if (other.privsId != null)
//				return false;
//		} else if (!privsId.equals(other.privsId))
//			return false;
//		if (roleId == null) {
//			if (other.roleId != null)
//				return false;
//		} else if (!roleId.equals(other.roleId))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "SysRolePrivs [roleId=" + roleId + ", privsId=" + privsId + "]";
//	}
//
//}