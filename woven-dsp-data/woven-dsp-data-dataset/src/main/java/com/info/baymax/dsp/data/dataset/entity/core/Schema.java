package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsListStringTypeHandler;
import com.info.baymax.dsp.data.dataset.entity.Maintable;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsListDataFieldTypeHandler;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_schema", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name", "version"})}, indexes = {
    @Index(columnList = "tenantId,resourceId"), @Index(columnList = "lastModifiedTime DESC")})
public class Schema extends Maintable implements ResourceId,Cloneable {
    private static final long serialVersionUID = -8031675892724730725L;

    @ApiModelProperty("资源目录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    @ApiModelProperty("字段列表")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsListDataFieldTypeHandler.class)
    private List<DataField> fields;

    @ApiModelProperty("模型")
    @Column(name = "schema_mode", length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String mode;// = "";

    @ApiModelProperty("主键列表")
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
    @Column(length = 50, name="o_id")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String oid;

    @ApiModelProperty("是否为最新记录,取值为1或0,默认为1")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    private Integer newest;


    public Schema() {
    }

    public Schema(String name, List<DataField> fields) {
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
