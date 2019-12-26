package com.info.baymax.common.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.entity.field.resolver.DefaultFieldResolver;
import com.info.baymax.common.entity.preprocess.PreEntity;
import com.info.baymax.common.saas.SaasContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public abstract class CommonEntity<ID extends Serializable> implements PreEntity, Serializable {
    private static final long serialVersionUID = 4394421573081538612L;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    public abstract ID getId();

    @ApiModelProperty("名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String name;

    @ApiModelProperty(value = "是否启用：0-未启用，1-启用，默认0", allowableValues = "0,1")
    @Column(length = 1)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    protected Integer enabled;

    @ApiModelProperty("租户ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String tenantId;

    @ApiModelProperty("所属人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String owner;

    @ApiModelProperty("创建人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String creator;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    protected Date createTime;

    @ApiModelProperty("修改人")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String lastModifier;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnType(jdbcType = JdbcType.TIMESTAMP)
    protected Date lastModifiedTime;

    @ApiModelProperty("描述信息")
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

        // 处理@DefaultValue注解，设置默认值
        DefaultFieldResolver fieldResolver = new DefaultFieldResolver();
        fieldResolver.resolve(this);
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