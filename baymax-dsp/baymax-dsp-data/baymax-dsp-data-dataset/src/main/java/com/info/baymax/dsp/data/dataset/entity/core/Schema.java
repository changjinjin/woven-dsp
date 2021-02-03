package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.persistence.entity.base.Maintable;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.base64.clob.GZBase64ClobVsListStringTypeHandler;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListFieldDescTypeHandler;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import com.merce.woven.common.FieldDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_schema", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name", "version"})}, indexes = {
    @Index(columnList = "tenantId,resourceId"), @Index(columnList = "lastModifiedTime DESC")})
@Comment("元数据信息表")
public class Schema extends Maintable implements ResourceId,Cloneable {
    private static final long serialVersionUID = -8031675892724730725L;

    @ApiModelProperty("资源目录ID")
    @Comment("资源目录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    @ApiModelProperty("字段列表")
    @Comment("字段列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListFieldDescTypeHandler.class)
    private List<FieldDesc> fields;

    @ApiModelProperty("模型")
    @Comment("模型")
    @Column(name = "schema_mode", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String mode;// = "";

    @ApiModelProperty("主键列表")
    @Comment("主键列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListStringTypeHandler.class)
    private List<String> primaryKeys;

    @ApiModelProperty("资源目录信息")
    @Transient
    private ResourceDesc resource;

    @ApiModelProperty("所属项目信息")
    @Transient
    private ProjectEntity projectEntity;

    @ApiModelProperty("原始记录id")
    @Comment("原始记录id")
    @Column(length = 50, name="o_id")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String oid;

    @ApiModelProperty("是否为最新记录,取值为1或0,默认为1")
    @Comment("是否为最新记录,取值为1或0,默认为1")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("1")
    private Integer newest;

    @ApiModelProperty("是否隐藏,1 隐藏，0 显示，默认值为0")
    @Comment("是否隐藏,1 隐藏，0 显示，默认值为0")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("0")
    private Integer isHide;

    public Schema() {
    }

    public Schema(String name, List<FieldDesc> fields) {
        this.name = name;
        this.fields = fields;
    }

    // 获取resourceId,如果为空则从resource中获取
    public String getResourceId() {
        if (resourceId == null && resource != null) {
            resourceId = resource.getId();
        }
        return resourceId;
    }

    @Override
    public Object clone(){
        return super.clone();
    }

}
