//package com.jusfoun.services.auth.api.entity;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import javax.persistence.Column;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.jusfoun.common.base.extend.entity.BaseEntity;
//import com.jusfoun.common.utils.ICollections;
//import com.jusfoun.services.ops.api.enums.AccountStatus;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
///**
// * 说明： 系统用户信息. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2018年1月5日 上午9:10:43
// */
//@ApiModel
//@Table(name = "sys_user")
//public class SysUser extends BaseEntity<SysUser> {
//	private static final long serialVersionUID = -1543606832463879178L;
//
//	/**
//	 * 登录名
//	 */
//	@ApiModelProperty("登录名")
//	private String username;
//
//	/**
//	 * 真实姓名
//	 */
//	@ApiModelProperty("真实姓名")
//	@Column(name = "real_name")
//	private String realName;
//
//	/**
//	 * 密码
//	 */
//	@ApiModelProperty("密码")
//	@Column(name = "password")
//	private String password;
//
//	/**
//	 * 所属机构标识符
//	 */
//	@ApiModelProperty("部门ID")
//	@Column(name = "gov_id")
//	private Long govId;
//
//	/**
//	 * 机构名称
//	 */
//	@ApiModelProperty("部门名称")
//	@Transient
//	private String govName;
//
//	/**
//	 * 性别
//	 */
//	@ApiModelProperty("性别")
//	private Integer gender;
//
//	/**
//	 * 固话
//	 */
//	@ApiModelProperty("电话")
//	private String tel;
//
//	/**
//	 * 手机号码
//	 */
//	@ApiModelProperty("手机")
//	private String mobile;
//
//	/**
//	 * 电子邮箱
//	 */
//	@ApiModelProperty("邮箱")
//	private String email;
//
//	/**
//	 * 用户地址
//	 */
//	@ApiModelProperty("地址")
//	private String address;
//
//	/**
//	 * 是否是系统管理员：0-否，1-是
//	 */
//	@ApiModelProperty("是否是系统管理员：0-否，1-是")
//	@Column(name = "is_admin")
//	private Boolean isAdmin;
//
//	/**
//	 * 用户状态
//	 * 
//	 * @see AccountStatus
//	 */
//	@ApiModelProperty("用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用")
//	@Column(name = "status")
//	private Byte status;
//
//	/**
//	 * 角色列表
//	 */
//	@ApiModelProperty("角色列表")
//	@Transient
//	@JsonInclude(Include.NON_NULL)
//	private Set<SysRole> sysRoles;
//
//	@ApiModelProperty("角色列表")
//	@Transient
//	@JsonInclude(Include.NON_NULL)
//	private Set<String> roleNames;
//
//	@ApiModelProperty("权限列表")
//	@Transient
//	private Set<String> authorities = new HashSet<>();
//
//	public void setAuthorities(Set<String> authorities) {
//		this.authorities = authorities;
//	}
//
//	public Set<String> getAuthorities() {
//		if (ICollections.hasElements(authorities)) {
//			return authorities;
//		}
//		Set<String> userAuthotities = new HashSet<String>();
//		sysRoles = getSysRoles();
//		if (ICollections.hasElements(sysRoles)) {
//			for (SysRole role : sysRoles) {
//				if (ICollections.hasElements(role.getSysPrivss())) {
//					userAuthotities.addAll(role.getSysPrivss().parallelStream().map(t -> t.getUrl()).collect(Collectors.toSet()));
//				}
//			}
//		}
//		return userAuthotities;
//	}
//
//	/**
//	 * 说明： 如果角色不为空则获取角色列表. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年9月23日 下午2:49:40
//	 * @return
//	 */
//	public Set<String> getRoleNames() {
//		if (ICollections.hasElements(getSysRoles())) {
//			return getSysRoles().parallelStream().map(t -> t.getName()).collect(Collectors.toSet());
//		}
//		return roleNames;
//	}
//
//	@Override
//	public String initOrderByClause() {
//		return "username ASC";
//	}
//
//	public void setRoleNames(Set<String> roleNames) {
//		this.roleNames = roleNames;
//	}
//
//	public String getGovName() {
//		return govName;
//	}
//
//	public void setGovName(String govName) {
//		this.govName = govName;
//	}
//
//	public Set<SysRole> getSysRoles() {
//		return sysRoles;
//	}
//
//	public void setSysRoles(Set<SysRole> sysRoles) {
//		this.sysRoles = sysRoles;
//	}
//
//	public Integer getGender() {
//		return gender;
//	}
//
//	public void setGender(Integer gender) {
//		this.gender = gender;
//	}
//
//	public String getTel() {
//		return tel;
//	}
//
//	public void setTel(String tel) {
//		this.tel = tel;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
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
//	/**
//	 * 获取登录名
//	 *
//	 * @return USER_NAME - 登录名
//	 */
//	public String getUsername() {
//		return username;
//	}
//
//	/**
//	 * 设置登录名
//	 *
//	 * @param userName
//	 *            登录名
//	 */
//	public void setUsername(String username) {
//		this.username = username == null ? null : username.trim();
//	}
//
//	/**
//	 * 获取密码
//	 *
//	 * @return PASSWORD - 密码
//	 */
//	public String getPassword() {
//		return password;
//	}
//
//	/**
//	 * 设置密码
//	 *
//	 * @param password
//	 *            密码
//	 */
//	public void setPassword(String password) {
//		this.password = password == null ? null : password.trim();
//	}
//
//	/**
//	 * 获取所属机构标识符
//	 *
//	 * @return GOV_ID - 所属机构标识符
//	 */
//	public Long getGovId() {
//		return govId;
//	}
//
//	/**
//	 * 设置所属机构标识符
//	 *
//	 * @param govId
//	 *            所属机构标识符
//	 */
//	public void setGovId(Long govId) {
//		this.govId = govId;
//	}
//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public Boolean getIsAdmin() {
//		return isAdmin;
//	}
//
//	public void setIsAdmin(Boolean isAdmin) {
//		this.isAdmin = isAdmin;
//	}
//
//	public boolean isAdmin() {
//		return isAdmin == null ? false : isAdmin;
//	}
//
//	public String getRealName() {
//		return realName;
//	}
//
//	public void setRealName(String realName) {
//		this.realName = realName;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((address == null) ? 0 : address.hashCode());
//		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
//		result = prime * result + ((govId == null) ? 0 : govId.hashCode());
//		result = prime * result + ((govName == null) ? 0 : govName.hashCode());
//		result = prime * result + ((isAdmin == null) ? 0 : isAdmin.hashCode());
//		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
//		result = prime * result + ((password == null) ? 0 : password.hashCode());
//		result = prime * result + ((realName == null) ? 0 : realName.hashCode());
//		result = prime * result + ((roleNames == null) ? 0 : roleNames.hashCode());
//		result = prime * result + ((sysRoles == null) ? 0 : sysRoles.hashCode());
//		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
//		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
//		SysUser other = (SysUser) obj;
//		if (address == null) {
//			if (other.address != null)
//				return false;
//		} else if (!address.equals(other.address))
//			return false;
//		if (authorities == null) {
//			if (other.authorities != null)
//				return false;
//		} else if (!authorities.equals(other.authorities))
//			return false;
//		if (email == null) {
//			if (other.email != null)
//				return false;
//		} else if (!email.equals(other.email))
//			return false;
//		if (gender == null) {
//			if (other.gender != null)
//				return false;
//		} else if (!gender.equals(other.gender))
//			return false;
//		if (govId == null) {
//			if (other.govId != null)
//				return false;
//		} else if (!govId.equals(other.govId))
//			return false;
//		if (govName == null) {
//			if (other.govName != null)
//				return false;
//		} else if (!govName.equals(other.govName))
//			return false;
//		if (isAdmin == null) {
//			if (other.isAdmin != null)
//				return false;
//		} else if (!isAdmin.equals(other.isAdmin))
//			return false;
//		if (mobile == null) {
//			if (other.mobile != null)
//				return false;
//		} else if (!mobile.equals(other.mobile))
//			return false;
//		if (password == null) {
//			if (other.password != null)
//				return false;
//		} else if (!password.equals(other.password))
//			return false;
//		if (realName == null) {
//			if (other.realName != null)
//				return false;
//		} else if (!realName.equals(other.realName))
//			return false;
//		if (roleNames == null) {
//			if (other.roleNames != null)
//				return false;
//		} else if (!roleNames.equals(other.roleNames))
//			return false;
//		if (sysRoles == null) {
//			if (other.sysRoles != null)
//				return false;
//		} else if (!sysRoles.equals(other.sysRoles))
//			return false;
//		if (tel == null) {
//			if (other.tel != null)
//				return false;
//		} else if (!tel.equals(other.tel))
//			return false;
//		if (username == null) {
//			if (other.username != null)
//				return false;
//		} else if (!username.equals(other.username))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "SysUser [username=" + username + ", realName=" + realName + ", password=" + password + ", govId=" + govId + ", govName=" + govName + ", gender=" + gender + ", tel="
//				+ tel + ", mobile=" + mobile + ", email=" + email + ", address=" + address + ", isAdmin=" + isAdmin + ", sysRoles=" + sysRoles + ", roleNames=" + roleNames
//				+ ", authorities=" + authorities + "]";
//	}
//
//}