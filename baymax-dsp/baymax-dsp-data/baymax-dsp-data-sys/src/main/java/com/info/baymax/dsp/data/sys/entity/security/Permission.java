package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.mybatis.genid.UuidGenId;
import com.info.baymax.common.persistence.service.tree.id.TreeIdable;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "permission")
@XmlType(name = "MercePermission")
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "dsp_sys_menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"clientId", "code"})})
@Comment("权限信息表")
public class Permission implements Idable<String>, Comparable<Permission>, TreeIdable<String, Permission> {
    private static final long serialVersionUID = 4953480541587178592L;

    @ApiModelProperty("主键")
    @Comment("主键")
    @Id
    @KeySql(genId = UuidGenId.class)
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String id;

    @XmlElement(name = "name")
    @ApiModelProperty("名称")
    @Comment("名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String name;

    @XmlElement(name = "clientId")
    @ApiModelProperty(value = "客户端ID")
    @Comment("客户端ID")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("'dsp'")
    private String clientId;

    @XmlElement(name = "enName")
    @ApiModelProperty(value = "英文名称")
    @Comment("英文名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String enName;

    @XmlElement(name = "code")
    @ApiModelProperty(value = "权限编码，每一个编码唯一")
    @Comment("权限编码，每一个编码唯一")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String code;

    @XmlTransient
    @ApiModelProperty("父节点ID")
    @Comment("父节点ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String parentId;

    @XmlElement(name = "url")
    @ApiModelProperty(value = "权限路径")
    @Comment("权限路径")
    @Column(length = 150)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String url;

    @XmlElement(name = "type")
    @ApiModelProperty(value = "权限类型：1-模块，2-菜单，3-按钮，4-文件，5-资源")
    @Comment("权限类型：1-模块，2-菜单，3-按钮，4-文件，5-资源")
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
    @Comment("图标样式")
    @Column(length = 150)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String icon;

    @XmlElement(name = "route")
    @ApiModelProperty(value = "前端路由")
    @Comment("前端路由")
    @Column(length = 150)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String route;

    @XmlElement(name = "order")
    @ApiModelProperty(value = "排序序号")
    @Comment("排序序号")
    @Column(name = "ord", length = 3)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("1")
    private Integer order;

    @ApiModelProperty(value = "是否是半选中状态：false-否（全选中），true-是，默认0")
    @Transient
    @XmlTransient
    private Boolean halfSelect;

    @ApiModelProperty(value = "关联的接口信息列表")
    @XmlElementWrapper(name = "operations")
    @XmlElement(name = "operation")
    @Transient
    private Set<RestOperation> operations;

    @XmlElement(name = "enabled")
    @ApiModelProperty("是否启用:0-未启用，1-启用")
    @Comment("是否启用:0-未启用，1-启用")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("1")
    protected Integer enabled;

    @XmlTransient
    @ApiModelProperty("描述信息")
    @Comment("描述信息")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String description;

    public Permission(String name, String url, Integer type, List<Permission> children) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.children = children;
    }

    public String getId() {
        if (StringUtils.isEmpty(id) && StringUtils.isNotEmpty(code)) {
            this.id = code;
        }
        return id;
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
