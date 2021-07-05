package com.info.baymax.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.gene.Enabled;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.entity.preprocess.PreEntity;
import com.info.baymax.common.persistence.mybatis.type.routing.DateTypeRoutingHandler;
import com.info.baymax.common.validation.constraints.MustIn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
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
public abstract class CommonEntity<ID extends Serializable> extends OwnerEntity<ID>
		implements Idable<ID>, Enabled<Integer>, PreEntity, Serializable {
	private static final long serialVersionUID = 4394421573081538612L;

	@ApiModelProperty(value = "名称", required = true)
	@Comment("名称")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@NotBlank
	@Field(type = FieldType.Text)
	protected String name;

	@ApiModelProperty(value = "是否启用：0-未启用，1-启用，默认0", allowableValues = "0,1", required = false)
	@Comment("是否启用：0-未启用，1-启用，默认0")
	@Column(length = 1)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ColumnDefault("1")
	@MustIn(value = { "0", "1" }, message = "Field [enabled] value must be in {values}")
	@Field(type = FieldType.Integer)
	protected Integer enabled;

	@ApiModelProperty(value = "创建人", required = false)
	@Comment("创建人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Text)
	protected String creator;

	@ApiModelProperty(value = "创建时间", required = false)
	@Comment("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP, typeHandler = DateTypeRoutingHandler.class)
	@Field(name = "create_time", type = FieldType.Text)
	protected Date createTime;

	@ApiModelProperty(value = "修改人", required = false)
	@Comment("修改人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(name = "last_modifier", type = FieldType.Text)
	protected String lastModifier;

	@ApiModelProperty(value = "修改时间", required = false)
	@Comment("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP, typeHandler = DateTypeRoutingHandler.class)
	@Field(name = "last_modified_time", type = FieldType.Text)
	protected Date lastModifiedTime;

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
	}

	@Override
	public void preUpdate() {
		this.setLastModifiedTime(new Date());
		String currentUserId = SaasContext.getCurrentUserId();
		if (StringUtils.isNotEmpty(currentUserId)) {
			this.setLastModifier(SaasContext.getCurrentUsername());
		}
	}

	public void copyCommonProperties(BaseEntity other) {
		this.tenantId = other.tenantId;
		this.owner = other.owner;
		this.creator = other.creator;
		this.lastModifier = other.lastModifier;
	}
}