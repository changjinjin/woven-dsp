//package com.jusfoun.services.auth.api.entity;
//
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.jusfoun.common.base.extend.entity.BaseEntity;
//import com.jusfoun.common.enums.types.UsingStatus;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
///**
// * 说明：系统角色信息. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2018年1月5日 上午9:10:08
// */
//@ApiModel
//@Table(name = "sys_role")
//public class SysRole extends BaseEntity<SysRole> {
//	private static final long serialVersionUID = -5436099250908436069L;
//
//	/**
//	 * 父节点id
//	 */
//	@ApiModelProperty("父节点id")
//	@Column(name = "parent_id")
//	private Long parentId;
//
//	/**
//	 * 角色名称
//	 */
//	@ApiModelProperty("角色名称")
//	private String name;
//
//	/**
//	 * 角色状态
//	 * 
//	 * @see UsingStatus
//	 */
//	@ApiModelProperty("0-启用、1-闲置")
//	private Byte status;
//
//	/**
//	 * 角色拥有的权限列表
//	 */
//	@ApiModelProperty("角色拥有的权限列表")
//	@Transient
//	@JsonInclude(Include.NON_NULL)
//	private Set<SysPrivs> sysPrivss;
//
//	public SysRole() {
//	}
//
//	public SysRole(Long id) {
//		super.setId(id);
//	}
//
//	public SysRole(Long id, Long parentId, String name) {
//		super.setId(id);
//		this.parentId = parentId;
//		this.name = name;
//	}
//
//	public Long getParentId() {
//		return parentId;
//	}
//
//	public void setParentId(Long parentId) {
//		this.parentId = parentId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Byte getStatus() {
//		return status;
//	}
//
//	public void setStatus(Byte status) {
//		this.status = status;
//	}
//
//	public Set<SysPrivs> getSysPrivss() {
//		return sysPrivss;
//	}
//
//	public void setSysPrivss(Set<SysPrivs> sysPrivss) {
//		this.sysPrivss = sysPrivss;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
//		result = prime * result + ((status == null) ? 0 : status.hashCode());
//		result = prime * result + ((sysPrivss == null) ? 0 : sysPrivss.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SysRole other = (SysRole) obj;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		if (parentId == null) {
//			if (other.parentId != null)
//				return false;
//		} else if (!parentId.equals(other.parentId))
//			return false;
//		if (status == null) {
//			if (other.status != null)
//				return false;
//		} else if (!status.equals(other.status))
//			return false;
//		if (sysPrivss == null) {
//			if (other.sysPrivss != null)
//				return false;
//		} else if (!sysPrivss.equals(other.sysPrivss))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "SysRole [parentId=" + parentId + ", name=" + name + ", status=" + status + ", sysPrivss=" + sysPrivss + ", id=" + id + ", createDate=" + createDate + ", creatorId="
//				+ creatorId + ", creatorName=" + creatorName + ", updateDate=" + updateDate + ", updaterId=" + updaterId + ", updaterName=" + updaterName + ", remark=" + remark
//				+ "]";
//	}
//
//}