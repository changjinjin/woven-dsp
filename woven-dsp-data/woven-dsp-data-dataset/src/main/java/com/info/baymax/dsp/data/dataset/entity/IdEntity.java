package com.info.baymax.dsp.data.dataset.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.info.baymax.common.mybatis.genid.UuidGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class IdEntity implements Serializable {
    private static final long serialVersionUID = 8199030533526416114L;

    @ApiModelProperty("主键")
    @Id
    @KeySql(genId = UuidGenId.class)
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String id;
}
