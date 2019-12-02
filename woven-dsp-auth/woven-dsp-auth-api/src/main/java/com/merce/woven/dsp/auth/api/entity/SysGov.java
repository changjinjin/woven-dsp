//package com.jusfoun.services.auth.api.entity;
//
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import com.jusfoun.common.base.extend.entity.BaseEntity;
//import com.jusfoun.common.base.tree.Treeable;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
///**
// * 说明：组织机构. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2018年1月5日 上午9:09:08
// */
//@ApiModel
//@Table(name = "sys_gov")
//public class SysGov extends BaseEntity<SysGov> implements Treeable<SysGov> {
//	private static final long serialVersionUID = 1135482541484948992L;
//
//	/**
//	 * 父节点标识符
//	 */
//	@ApiModelProperty("父节点标识符")
//	@Column(name = "parent_id")
//	private Long parentId;
//	/**
//	 * 父节点标名称
//	 */
//	@ApiModelProperty("父节点标名称")
//	@Transient
//	private String parentName;
//
//	/**
//	 * 中文全称
//	 */
//	@ApiModelProperty("中文全称")
//	@Column(name = "full_name")
//	private String fullName;
//
//	/**
//	 * 中文简称
//	 */
//	@ApiModelProperty("中文简称")
//	@Column(name = "short_name")
//	private String shortName;
//
//	/**
//	 * 机构地址
//	 */
//	@ApiModelProperty("机构地址")
//	private String address;
//
//	/**
//	 * 联系人
//	 */
//	@ApiModelProperty("联系人")
//	private String contacts;
//
//	/**
//	 * 联系电话
//	 */
//	@ApiModelProperty("联系电话")
//	private String tel;
//
//	/**
//	 * 电子邮箱
//	 */
//	@ApiModelProperty("电子邮箱")
//	private String email;
//
//	/**
//	 * 网址
//	 */
//	@ApiModelProperty("网址")
//	private String weburl;
//
//	/**
//	 * 行政区划
//	 */
//	@ApiModelProperty("行政区划")
//	private String area;
//
//	/**
//	 * 子节点集合
//	 */
//	@ApiModelProperty("子节点集合")
//	@Transient
//	private List<SysGov> subList;
//
//	/**
//	 * 是否是叶子节点
//	 */
//	@ApiModelProperty("是否是叶子节点")
//	@Transient
//	private boolean leaf;
//
//	@Override
//	public String initOrderByClause() {
//		return "full_name ASC";
//	}
//
//	@Override
//	public List<SysGov> getSubList() {
//		return subList;
//	}
//
//	@Override
//	public void setSubList(List<SysGov> subList) {
//		this.subList = subList;
//	}
//
//	@Override
//	public String[] matchFeilds() {
//		return new String[]{fullName, shortName};
//	}
//
//	@Override
//	public void setLeaf(boolean leaf) {
//		this.leaf = leaf;
//	}
//
//	@Override
//	public boolean isLeaf() {
//		return leaf;
//	}
//
//	public String getParentName() {
//		return parentName;
//	}
//
//	public void setParentName(String parentName) {
//		this.parentName = parentName;
//	}
//
//	/**
//	 * 获取父节点标识符
//	 *
//	 * @return parent_id - 父节点标识符
//	 */
//	public Long getParentId() {
//		return parentId;
//	}
//
//	/**
//	 * 设置父节点标识符
//	 *
//	 * @param parentId
//	 *            父节点标识符
//	 */
//	public void setParentId(Long parentId) {
//		this.parentId = parentId;
//	}
//
//	/**
//	 * 获取中文全称
//	 *
//	 * @return full_name - 中文全称
//	 */
//	public String getFullName() {
//		return fullName;
//	}
//
//	/**
//	 * 设置中文全称
//	 *
//	 * @param fullName
//	 *            中文全称
//	 */
//	public void setFullName(String fullName) {
//		this.fullName = fullName == null ? null : fullName.trim();
//	}
//
//	/**
//	 * 获取中文简称
//	 *
//	 * @return short_name - 中文简称
//	 */
//	public String getShortName() {
//		return shortName;
//	}
//
//	/**
//	 * 设置中文简称
//	 *
//	 * @param shortName
//	 *            中文简称
//	 */
//	public void setShortName(String shortName) {
//		this.shortName = shortName == null ? null : shortName.trim();
//	}
//
//	/**
//	 * 获取机构地址
//	 *
//	 * @return address - 机构地址
//	 */
//	public String getAddress() {
//		return address;
//	}
//
//	/**
//	 * 设置机构地址
//	 *
//	 * @param address
//	 *            机构地址
//	 */
//	public void setAddress(String address) {
//		this.address = address == null ? null : address.trim();
//	}
//
//	/**
//	 * 获取联系人
//	 *
//	 * @return contacts - 联系人
//	 */
//	public String getContacts() {
//		return contacts;
//	}
//
//	/**
//	 * 设置联系人
//	 *
//	 * @param contacts
//	 *            联系人
//	 */
//	public void setContacts(String contacts) {
//		this.contacts = contacts == null ? null : contacts.trim();
//	}
//
//	/**
//	 * 获取联系电话
//	 *
//	 * @return tel - 联系电话
//	 */
//	public String getTel() {
//		return tel;
//	}
//
//	/**
//	 * 设置联系电话
//	 *
//	 * @param tel
//	 *            联系电话
//	 */
//	public void setTel(String tel) {
//		this.tel = tel == null ? null : tel.trim();
//	}
//
//	/**
//	 * 获取电子邮箱
//	 *
//	 * @return email - 电子邮箱
//	 */
//	public String getEmail() {
//		return email;
//	}
//
//	/**
//	 * 设置电子邮箱
//	 *
//	 * @param email
//	 *            电子邮箱
//	 */
//	public void setEmail(String email) {
//		this.email = email == null ? null : email.trim();
//	}
//
//	/**
//	 * 获取网址
//	 *
//	 * @return weburl - 网址
//	 */
//	public String getWeburl() {
//		return weburl;
//	}
//
//	/**
//	 * 设置网址
//	 *
//	 * @param weburl
//	 *            网址
//	 */
//	public void setWeburl(String weburl) {
//		this.weburl = weburl == null ? null : weburl.trim();
//	}
//
//	/**
//	 * 获取行政区划
//	 *
//	 * @return area - 行政区划
//	 */
//	public String getArea() {
//		return area;
//	}
//
//	/**
//	 * 设置行政区划
//	 *
//	 * @param area
//	 *            行政区划
//	 */
//	public void setArea(String area) {
//		this.area = area == null ? null : area.trim();
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((address == null) ? 0 : address.hashCode());
//		result = prime * result + ((area == null) ? 0 : area.hashCode());
//		result = prime * result + ((contacts == null) ? 0 : contacts.hashCode());
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
//		result = prime * result + (leaf ? 1231 : 1237);
//		result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
//		result = prime * result + ((parentName == null) ? 0 : parentName.hashCode());
//		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
//		result = prime * result + ((subList == null) ? 0 : subList.hashCode());
//		result = prime * result + ((tel == null) ? 0 : tel.hashCode());
//		result = prime * result + ((weburl == null) ? 0 : weburl.hashCode());
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
//		SysGov other = (SysGov) obj;
//		if (address == null) {
//			if (other.address != null)
//				return false;
//		} else if (!address.equals(other.address))
//			return false;
//		if (area == null) {
//			if (other.area != null)
//				return false;
//		} else if (!area.equals(other.area))
//			return false;
//		if (contacts == null) {
//			if (other.contacts != null)
//				return false;
//		} else if (!contacts.equals(other.contacts))
//			return false;
//		if (email == null) {
//			if (other.email != null)
//				return false;
//		} else if (!email.equals(other.email))
//			return false;
//		if (fullName == null) {
//			if (other.fullName != null)
//				return false;
//		} else if (!fullName.equals(other.fullName))
//			return false;
//		if (leaf != other.leaf)
//			return false;
//		if (parentId == null) {
//			if (other.parentId != null)
//				return false;
//		} else if (!parentId.equals(other.parentId))
//			return false;
//		if (parentName == null) {
//			if (other.parentName != null)
//				return false;
//		} else if (!parentName.equals(other.parentName))
//			return false;
//		if (shortName == null) {
//			if (other.shortName != null)
//				return false;
//		} else if (!shortName.equals(other.shortName))
//			return false;
//		if (subList == null) {
//			if (other.subList != null)
//				return false;
//		} else if (!subList.equals(other.subList))
//			return false;
//		if (tel == null) {
//			if (other.tel != null)
//				return false;
//		} else if (!tel.equals(other.tel))
//			return false;
//		if (weburl == null) {
//			if (other.weburl != null)
//				return false;
//		} else if (!weburl.equals(other.weburl))
//			return false;
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "SysGov [parentId=" + parentId + ", parentName=" + parentName + ", fullName=" + fullName + ", shortName=" + shortName + ", address=" + address + ", contacts="
//				+ contacts + ", tel=" + tel + ", email=" + email + ", weburl=" + weburl + ", area=" + area + ", subList=" + subList + ", leaf=" + leaf + "]";
//	}
//	
//
//}