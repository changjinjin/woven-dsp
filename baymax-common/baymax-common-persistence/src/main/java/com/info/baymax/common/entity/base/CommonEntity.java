package com.info.baymax.common.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.entity.gene.Enabled;
import com.info.baymax.common.entity.gene.Idable;
import com.info.baymax.common.entity.preprocess.PreEntity;
import com.info.baymax.common.entity.validation.MustIn;
import com.info.baymax.common.saas.SaasContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public abstract class CommonEntity<ID extends Serializable>
		implements Idable<ID>, Enabled<Integer>, PreEntity, Serializable {
	private static final long serialVersionUID = 4394421573081538612L;

	@ApiModelProperty(value = "名称", required = true)
	@Comment("名称")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@NotBlank
	protected String name;

	@ApiModelProperty(value = "是否启用：0-未启用，1-启用，默认0", allowableValues = "0,1", required = false)
	@Comment("是否启用：0-未启用，1-启用，默认0")
	@Column(length = 1)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ColumnDefault("1")
	@MustIn(value = { "0", "1" }, message = "Field [enabled] value must be in {values}")
	protected Integer enabled;

	@ApiModelProperty(value = "租户ID", required = false)
	@Comment("租户ID")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String tenantId;

	@ApiModelProperty(value = "所属人", required = false)
	@Comment("所属人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String owner;

	@ApiModelProperty(value = "创建人", required = false)
	@Comment("创建人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String creator;

	@ApiModelProperty(value = "创建时间", required = false)
	@Comment("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	protected Date createTime;

	@ApiModelProperty(value = "修改人", required = false)
	@Comment("修改人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String lastModifier;

	@ApiModelProperty(value = "修改时间", required = false)
	@Comment("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP)
	protected Date lastModifiedTime;

	@ApiModelProperty(value = "描述信息", required = false)
	@Comment("描述信息")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String description;

	@Override
	public void preInsert() {
		String currentTenantId = SaasContext.getCurrentTenantId();
		if (StringUtils.isNotEmpty(currentTenantId)) {
			this.setTenantId(currentTenantId);
		}

		Date createTime = new Date();
		this.setCreateTime(createTime);
		this.setLastModifiedTime(createTime);

		String currentUserId = SaasContext.getCurrentUserId();
		if (StringUtils.isNotEmpty(currentUserId)) {
			if (StringUtils.isEmpty(owner)) {
				this.setOwner(currentUserId);
			}
			if (StringUtils.isEmpty(creator)) {
				this.setCreator(SaasContext.getCurrentUsername());
			}
			if (StringUtils.isEmpty(lastModifier)) {
				this.setLastModifier(SaasContext.getCurrentUsername());
			}
		}

		// 处理@ColumnDefault注解，设置默认值
		// DefaultFieldResolver.getInstance().resolve(this);
	}

	@Override
	public void preUpdate() {
		this.setLastModifiedTime(new Date());
		String currentUserId = SaasContext.getCurrentUserId();
		if (StringUtils.isNotEmpty(currentUserId)) {
			this.setLastModifier(SaasContext.getCurrentUsername());
		}
	}
}