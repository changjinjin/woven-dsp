package com.merce.woven.common.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merce.woven.common.entity.field.DefaultValue;
import com.merce.woven.common.entity.field.resolver.DefaultFieldResolver;
import com.merce.woven.common.entity.preprocess.PreEntity;
import com.merce.woven.common.saas.SaasContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public class BaseEntity extends SnowFlakeIdEntity implements PreEntity {
	private static final long serialVersionUID = 4394421573081538612L;

	@ApiModelProperty("名称")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String name;

	@ApiModelProperty("创建人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String creator;

	@ApiModelProperty("创建时间")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	protected Date createTime;

	@ApiModelProperty("修改人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String lastModifier;

	@ApiModelProperty("修改时间")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	protected Date lastModifiedTime;

	@ApiModelProperty("所属人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@DefaultValue("SYSTEM")
	protected Long owner;

	@ApiModelProperty("是否启用")
	@Column(length = 1)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@DefaultValue("1")
	protected Integer enabled;

	@ApiModelProperty("描述简介")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String description;

	@ApiModelProperty("所属租户ID")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected Long tenantId;

	@Override
	public void preInsert() {
		Long currentTenantId = SaasContext.getCurrentTenantId();
		if (tenantId == null && currentTenantId != null) {
			this.setTenantId(currentTenantId);
		}

		Date createTime = new Date();
		this.setCreateTime(createTime);
		this.setLastModifiedTime(createTime);

		Long currentUserId = SaasContext.getCurrentUserId();
		if (currentUserId != null) {
			if (owner == null) {
				this.setOwner(currentUserId);
			}
			if (creator == null) {
				this.setCreator(SaasContext.getCurrentUsername());
			}
			if (lastModifier == null) {
				this.setLastModifier(SaasContext.getCurrentUsername());
			}
		}

		DefaultFieldResolver fieldResolver = new DefaultFieldResolver();
		fieldResolver.resolve(this);
	}

	@Override
	public void preUpdate() {
		this.setLastModifiedTime(new Date());
		Long currentUserId = SaasContext.getCurrentUserId();
		if (currentUserId != null) {
			this.setLastModifier(SaasContext.getCurrentUsername());
		}
	}
}