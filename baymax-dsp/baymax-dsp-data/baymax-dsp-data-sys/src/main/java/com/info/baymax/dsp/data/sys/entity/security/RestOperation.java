package com.info.baymax.dsp.data.sys.entity.security;

import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.persistence.mybatis.type.varchar.VarcharVsStringListTypeHandler;
import com.info.baymax.common.utils.HashUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(doNotUseGetters = true)
@ApiModel
@Entity
@Table(name = "merce_rest_operation", indexes = {@Index(columnList = "serviceName"), @Index(columnList = "summary")})
@Comment("租户信息表")
public class RestOperation implements Idable<String> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @Comment("主键")
    @Id
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String id;

    @ApiModelProperty("服务名称")
    @Comment("服务名称")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String serviceName;

    @ApiModelProperty(value = "接口所属分组")
    @Comment("接口所属分组")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String groupName;

    @ApiModelProperty(value = "接口所属标签")
    @Comment("接口所属标签")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsStringListTypeHandler.class)
    @Convert(converter = ObjectToStringConverter.class)
    private List<String> tags;

    @ApiModelProperty(value = "接口请求方法")
    @Comment("接口请求方法")
    @Column(length = 10)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String method;

    @ApiModelProperty(value = "接口基本路径")
    @Comment("接口基本路径")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String basePath;

    @ApiModelProperty(value = "接口相对路径")
    @Comment("接口相对路径")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String relativePath;

    @ApiModelProperty(value = "接口全路径")
    @Comment("接口全路径")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String fullPath;

    @ApiModelProperty(value = "接口概要")
    @Comment("接口概要")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String summary;

    @ApiModelProperty(value = "接口描述")
    @Comment("接口描述")
    @Column(length = 1000)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String description;

    @ApiModelProperty(value = "接口操作ID")
    @Comment("接口操作ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String operationId;

    @ApiModelProperty(value = "接口消费信息")
    @Comment("接口消费信息")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsStringListTypeHandler.class)
    @Convert(converter = ObjectToStringConverter.class)
    private List<String> consumes;

    @ApiModelProperty(value = "接口生产信息")
    @Comment("接口生产信息")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR, typeHandler = VarcharVsStringListTypeHandler.class)
    @Convert(converter = ObjectToStringConverter.class)
    private List<String> produces;

    @ApiModelProperty(value = "接口是否弃用：true-是，false-否")
    @Comment("接口是否弃用：true-是，false-否")
    @Column(length = 1)
    @ColumnType(jdbcType = JdbcType.BIT, typeHandler = BooleanTypeHandler.class)
    @ColumnDefault("0")
    private Boolean deprecated;

    @ApiModelProperty(value = "是否启用权限控制：true-是，false-否，默认false")
    @Comment("是否启用权限控制：true-是，false-否，默认false")
    @Column(length = 1)
    @ColumnType(jdbcType = JdbcType.BIT, typeHandler = BooleanTypeHandler.class)
    @ColumnDefault("0")
    private Boolean enabled;

    public RestOperation(String serviceName, String groupName, List<String> tags, String method, String basePath,
                         String relativePath, String summary, String description, String operationId, List<String> consumes,
                         List<String> produces, Boolean deprecated) {
        super();
        this.serviceName = serviceName;
        this.groupName = groupName;
        this.tags = tags;
        this.method = method;
        this.basePath = basePath;
        this.relativePath = relativePath;
        this.summary = summary;
        this.description = description;
        this.operationId = operationId;
        this.consumes = consumes;
        this.produces = produces;
        this.deprecated = deprecated;
    }

    public String getDescription() {
        if (StringUtils.isEmpty(description)) {
            this.description = this.summary;
        }
        return description;
    }

    public String getFullPath() {
        if (StringUtils.isEmpty(fullPath) && basePath != null && relativePath != null) {
            this.fullPath = trimSlash(getBasePath() + getRelativePath());
        }
        return fullPath;
    }

    public String getId() {
		if (StringUtils.isEmpty(id) && StringUtils.isNotEmpty(getFullPath())) {
			this.id = HashUtil.hashKey(operationKey());
		}
		return id;
	}

    public String operationKey() {
        return trimSlash(getFullPath() + "@" + getMethod());
    }

    private String trimSlash(String src) {
        return src.replaceAll("//", "/");
    }
}
