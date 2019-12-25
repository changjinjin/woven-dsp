package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.entity.base.Maintable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_udf", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "owner"})})
public class ProcessConfig extends Maintable {
    private static final long serialVersionUID = -7126302005615058755L;

    @ApiModelProperty("类名")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String className;

    @ApiModelProperty("配置类型")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String processConfigType;

    @ApiModelProperty("jar包路径")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String jarpath;

    @ApiModelProperty("参数列表")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String parameterlist;

    @ApiModelProperty("返回类型")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String returnType;

    @ApiModelProperty("返回值java类型")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String returnJavaType;

    @ApiModelProperty("别名")
    @Column(length = 100)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String aliasName;

    @ApiModelProperty("jar包名称")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String jarName;

    @ApiModelProperty("数据库类型")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String dbType;

    public ProcessConfig() {
    }

    public static ProcessConfig apply(String name, String className, String processConfigType, String jarpath, String parameterlist, String returnType) {
        return new ProcessConfig(name, className, processConfigType, jarpath, parameterlist, returnType);
    }

    public ProcessConfig(String name, String className, String processConfigType, String jarpath, String parameterlist, String returnType) {
        this.name = name;
        this.className = className;
        this.processConfigType = processConfigType;
        this.jarpath = jarpath;
        this.parameterlist = parameterlist;
        this.returnType = returnType;
    }
}
