package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Date;

/**
 * create by pengchuan.chen on 2019/11/27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@JsonPropertyOrder({"id", "name", "group", "creator", "createTime", "lastModifier", "lastModifiedTime", "owner",
        "description", "group", "version", "enabled", "livyUrl", "hdfsUrl"})
@Entity
@Table(name = "merce_cluster_info", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})},
        indexes = {@Index(columnList = "name"), @Index(columnList = "lastModifiedTime DESC")})
public class ClusterEntity<T> extends Maintable {

  @ApiModelProperty("df-executor注册信息.")
  @Transient
  protected Collection<T> dfInstance;

  @ApiModelProperty("可用的df-executor个数.")
  @Transient
  protected Integer dfCount;

  @ApiModelProperty("上传HADOOP_CONF的zip文件的id.")
  @Transient
  protected String fileId;

  @ApiModelProperty("hdfs访问地址(dfs.namenode.rpc-address)")
  @Column(length = 32)
  @ColumnType(jdbcType = JdbcType.VARCHAR)
  protected String hdfsUrl;

  @ApiModelProperty("Livy访问地址")
  @Column(length = 50)
  @ColumnType(jdbcType = JdbcType.VARCHAR)
  protected String livyUrl;

  @ApiModelProperty("HADOOP_CONF的zip文件")
  @Lob
  @ColumnType(jdbcType = JdbcType.BLOB)
  protected byte[] configFile;

  public ClusterEntity() {
  }

  public ClusterEntity(String name, String livyUrl, String hdfsUrl) {
    this.name = name;
    this.livyUrl = livyUrl;
    this.hdfsUrl = hdfsUrl;
    Date date = new Date();
    this.createTime = date;
    this.lastModifiedTime = date;
    this.version = 1;
    this.enabled = 1;
  }

}
