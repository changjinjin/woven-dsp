package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.persistence.entity.base.Maintable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_project")
@Comment("项目信息表")
public class ProjectEntity extends Maintable {
    private static final long serialVersionUID = -4579648692041993047L;

    @ApiModelProperty("子级列表")
    @Transient
    private List<ProjectEntity> children = new ArrayList<ProjectEntity>();

    @ApiModelProperty("资源类型")
    @Transient
    private String resType;

    public ProjectEntity() {
    }

    public ProjectEntity(String id, String name, String resType) {
        this.id = id;
        this.name = name;
        this.resType = resType;
    }
}
