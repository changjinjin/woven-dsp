package com.info.baymax.common.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.entity.preprocess.PreEntity;
import com.info.baymax.common.mybatis.genid.SnowflakeGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class BaseEntity extends CommonEntity<Long> implements PreEntity {
    private static final long serialVersionUID = -5099886601993514950L;

    @ApiModelProperty("主键")
    @Comment("主键")
    @Id
    @KeySql(genId = SnowflakeGenId.class)
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    protected Long id;

}