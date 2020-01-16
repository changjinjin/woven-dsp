package com.info.baymax.common.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.common.mybatis.genid.SnowflakeGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class SnowFlakeIdEntity implements Serializable {
    private static final long serialVersionUID = 8199030533526416114L;

    @ApiModelProperty("主键")
    @Id
    @KeySql(genId = SnowflakeGenId.class)
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @JsonSerialize(using = ToStringSerializer.class)
    protected Long id;
}
