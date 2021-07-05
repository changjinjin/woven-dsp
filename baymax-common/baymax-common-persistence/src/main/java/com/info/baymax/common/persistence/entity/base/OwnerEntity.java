package com.info.baymax.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.entity.preprocess.PreEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 将租户ID和用户ID属性提取出来，以避免继承MainTable类而继承大量冗余的属性
 *
 * @author jingwei.yang
 * @date 2020年3月26日 下午7:18:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public abstract class OwnerEntity<ID extends Serializable> implements Idable<ID>, PreEntity {
	private static final long serialVersionUID = 4394421573081538612L;

	@ApiModelProperty("所属租户ID")
	@Comment("所属租户ID")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(name = "tenant_id", type = FieldType.Keyword)
	protected String tenantId;

	@ApiModelProperty("所属人")
	@Comment("所属人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Field(type = FieldType.Keyword)
	protected String owner;

	@Override
	public void preInsert() {
		if (StringUtils.isEmpty(tenantId)) {
			this.setTenantId(SaasContext.getCurrentTenantId());
		}
		if (StringUtils.isEmpty(this.owner)) {
			this.setOwner(SaasContext.getCurrentUserId());
		}
	}
}
