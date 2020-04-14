package com.info.baymax.dsp.data.platform.entity;

import com.info.baymax.common.entity.base.BaseEntity;
import com.info.baymax.common.entity.field.DefaultValue;
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

/**
 * @Author: haijun
 * @Date: 2019/12/13 18:55 数据目录信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dsp_data_category", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "parentId", "name"})})
@Comment("数据目录信息表")
public class DataCategory extends BaseEntity implements Comparable<DataCategory>, TreeIdable<Long, DataCategory> {
    private static final long serialVersionUID = -2838835837038572690L;

    @ApiModelProperty("排序序号")
    @Comment("排序序号")
    @Column(name = "ord", length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    private Integer order;

    @ApiModelProperty("数据资源目录路径")
    @Comment("数据资源目录路径")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String path;

    @ApiModelProperty("父节点ID")
    @Comment("父节点ID")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long parentId;

    @ApiModelProperty("子级节点列表")
    @Comment("子级节点列表")
    @Transient
    private List<DataCategory> children = new ArrayList<DataCategory>();

    @ApiModelProperty(value = "是否是全选状态：false-否（半选中），true-是，默认0")
    @Transient
    protected Boolean halfSelect = true;

    @Override
    public int compareTo(DataCategory r) {
        if (this.order == null) {
            return 1;
        } else if (r.getOrder() == null) {
            return -1;
        }
        return this.order - r.getOrder();
    }

    // 按正序排列
    public List<DataCategory> getChildren() {
        if (ICollections.hasElements(children)) {
            Collections.sort(children);
        }
        return children;
    }
}
