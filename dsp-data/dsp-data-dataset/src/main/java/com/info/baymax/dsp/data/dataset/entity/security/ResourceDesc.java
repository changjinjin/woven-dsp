package com.info.baymax.dsp.data.dataset.entity.security;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.info.baymax.common.entity.base.Maintable;
import org.hibernate.annotations.ColumnDefault;
import com.info.baymax.common.service.tree.id.TreeIdable;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;

import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@JsonPropertyOrder({"id", "name", "description", "resType", "attributes", "parentId", "rootId", "index", "sharedUsers",
    "creator", "createTime", "lastModifier", "lastModifiedTime", "owner", "enabled", "order", "isHide"})
@Entity
@Table(name = "merce_resource_dir", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId","parentId", "name"})})
@Comment("资源目录信息表")
public class ResourceDesc extends Maintable implements Comparable<ResourceDesc>, TreeIdable<String, ResourceDesc> {
    private static final long serialVersionUID = -3618881429269900030L;

    @ApiModelProperty("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    @Comment("资源目录类型：schema_dir，dataset_dir，datasource_dir，standard_dir，flow_dir，fileset_dir")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resType;

    @ApiModelProperty("父节点ID")
    @Comment("父节点ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String parentId;

    @ApiModelProperty("排序序号")
    @Comment("排序序号")
    @Column(name = "ord", length = 11)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnDefault("1")
    private Integer order;

    @ApiModelProperty("是否隐藏，对于qa的目录文件夹不展示，1 隐藏，0 显示，默认值为0")
    @Comment("是否隐藏，对于qa的目录文件夹不展示，1 隐藏，0 显示，默认值为0")
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer isHide;

    @ApiModelProperty("资源目录路径")
    @Comment("资源目录路径")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String path;

    @ApiModelProperty("子级节点列表")
    @Comment("子级节点列表")
    @Transient
    private List<ResourceDesc> children = new ArrayList<ResourceDesc>();

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Comment("是否是全选状态：false-否（半选中），true-是，默认0")
    @Transient
    protected Boolean halfSelect = true;

    @Override
    public int compareTo(ResourceDesc r) {
        if (this.order == null) {
            return 1;
        } else if (r.getOrder() == null) {
            return -1;
        }
        return this.order - r.getOrder();
    }

    // 按正序排列
    @Override
    public List<ResourceDesc> getChildren() {
        if (ICollections.hasElements(children)) {
            Collections.sort(children);
        }
        return children;
    }

}
