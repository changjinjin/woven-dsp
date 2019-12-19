package com.info.baymax.dsp.data.sys.entity.security;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.ibatis.type.JdbcType;

import com.info.baymax.common.service.tree.id.TreeIdable;
import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.utils.ICollections;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tk.mybatis.mapper.annotation.ColumnType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "permission")
@XmlType(name = "MercePermission")
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_sys_menu", uniqueConstraints = { @UniqueConstraint(columnNames = { "clientId", "code" }) })
public class Permission extends BaseEntity implements Comparable<Permission>, TreeIdable<Long, Permission> {
	private static final long serialVersionUID = 4953480541587178592L;

	@XmlElement(name = "客户端ID")
	@ApiModelProperty(value = "客户端ID")
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String clientId;

	@XmlElement(name = "code")
	@ApiModelProperty(value = "权限编码，每一个编码唯一")
	@Column(length = 20)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String code;

	@XmlTransient
	@ApiModelProperty("父节点ID")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private Long parentId;

	@XmlElement(name = "url")
	@ApiModelProperty(value = "权限路径")
	@Column(length = 150)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String url;

	@XmlElement(name = "type")
	@ApiModelProperty(value = "权限类型：1-模块，2-菜单，3-按钮，4-文件，5-资源")
	@Column(length = 2)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	private Integer type;

	@XmlElementWrapper(name = "children")
	@XmlElement(name = "permission")
	@ApiModelProperty(value = "子级权限列表")
	@Transient
	private List<Permission> children;

	@XmlElement(name = "icon")
	@ApiModelProperty(value = "图标样式")
	@Column(length = 150)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String icon;

	@XmlElement(name = "route")
	@ApiModelProperty(value = "前端路由")
	@Column(length = 150)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	private String route;

	@XmlElement(name = "order")
	@ApiModelProperty(value = "排序序号")
	@Column(name = "ord", length = 3)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@DefaultValue("1")
	private Integer order;

	@ApiModelProperty(value = "是否是半选中状态：false-否（全选中），true-是，默认0")
	@Transient
	private Boolean halfSelect;

	public Permission() {
	}

	public Permission(String name, String url, Integer type, List<Permission> children) {
		this.name = name;
		this.url = url;
		this.type = type;
		this.children = children;
	}

	@XmlElement(name = "name")
	@Override
	public String getName() {
		return super.getName();
	}

	@XmlElement(name = "enabled")
	@Override
	public Integer getEnabled() {
		return super.getEnabled();
	}

	@Override
	public int compareTo(Permission r) {
		if (this.order == null) {
			return 1;
		} else if (r.getOrder() == null) {
			return -1;
		}
		return this.order - r.getOrder();
	}

	public List<Permission> getChildren() {
		if (ICollections.hasElements(children)) {
			Collections.sort(children);
		}
		return children;
	}
}
