package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.common.entity.field.DefaultValue;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.common.mybatis.type.base64.clob.GZBase64ClobVsMapStringKeyStringValueTypeHandler;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_dataset", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "tenantId,resourceId"), @Index(columnList = "tenantId,storage,resourceId"),
    @Index(columnList = "lastModifiedTime DESC"), @Index(columnList = "expiredTime")})
public class Dataset extends Maintable implements ResourceId, CryptoBean {
    private static final long serialVersionUID = 7644481936552526180L;

    @ApiModelProperty("过期时间")
    @Transient
    private Long expiredPeriod = 0L;

    @ApiModelProperty("资源目录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    @ApiModelProperty("元数据记录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String schemaId;

    @ApiModelProperty("元数据版本号")
    @Column(length = 10)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @DefaultValue("1")
    private Integer schemaVersion;

    @ApiModelProperty("元数据信息")
    @Transient
    private Schema schema;

    @ApiModelProperty("来源：input,output")
    @Column(length = 10)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String source;

    @ApiModelProperty("数据记录数")
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @Column(length = 20)
    @DefaultValue("0")
    private Long recordNumber;// = 0l;

    @ApiModelProperty("数据字节数")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long byteSize;// = 0l;

    @ApiModelProperty("存储格式:HDFS, HIVE, JDBC, KAFKA, HBASE, FTP, ElasticSearch, SearchOne, REDIS")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String storage;

    @ApiModelProperty("分片类型：H, D, F, Q, 1, 5")
    @Column(length = 2)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    @DefaultValue("H")
    private String sliceType;// = "H";

    @ApiModelProperty("分片时间")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String sliceTime;

    @ApiModelProperty("分析时间")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @DefaultValue("0")
    private Long analysisTime;// = 0l;

    @ApiModelProperty("存储配置参数")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> storageConfigurations;

    @ApiModelProperty("格式配置参数")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsMapStringKeyStringValueTypeHandler.class)
    private Map<String, String> formatConfigurations;

    @ApiModelProperty("所属资源目录信息")
    @Transient
    private ResourceDesc resource;

    @ApiModelProperty("所属项目信息")
    @Transient
    private ProjectEntity projectEntity;

    public Dataset() {
        super();
    }

    public Dataset(String name, Schema schema, String storage, Map<String, String> storageConfigurations) {
        this();
        this.name = name;
        this.schema = schema;
        this.storage = storage;
        this.storageConfigurations = storageConfigurations;
    }

    public String getSchemaId() {
        if (schemaId == null && schema != null) {
            schemaId = schema.getId();
        }
        return schemaId;
    }

    // 获取resourceId,如果为空则从resource中获取
    public String getResourceId() {
        if (resourceId == null && resource != null) {
            resourceId = resource.getId();
        }
        return resourceId;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
        if (expiredPeriod != null && this.expiredPeriod != 0) {
            setExpiredPeriod(this.expiredPeriod);
        }
    }

    @Override
    public void setExpiredPeriod(Long expiredPeriod) {
        this.expiredPeriod = expiredPeriod;
        if (expiredPeriod == 0) {
            expiredTime = MAX_DATE_TIME;
        } else {
            if (createTime == null) {
                createTime = new Date();
            }
            expiredTime = createTime.getTime() / 1000 + expiredPeriod;
        }
    }

    @Transient
    @Override
    public Long getExpiredPeriod() {
        if (expiredTime == null || expiredTime == 0 || expiredTime.longValue() == MAX_DATE_TIME.longValue()) {
            this.expiredPeriod = 0L;
        } else {
            this.expiredPeriod = expiredTime - createTime.getTime() / 1000;
        }
        return this.expiredPeriod;
    }

    @Override
    public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (storageConfigurations != null && !storageConfigurations.isEmpty()) {
            String password = storageConfigurations.get("password");
            if (password != null) {
                storageConfigurations.replace("password", ciphertext(password, cryptoType, cryptorDelegater));
            }
        }
    }

    @Override
    public void decrypt(CryptorDelegater cryptorDelegater) {
        if (storageConfigurations != null && !storageConfigurations.isEmpty()) {
            String password = storageConfigurations.get("password");
            if (password != null) {
                storageConfigurations.replace("password", plaintext(password, cryptorDelegater));
            }
            String sql = storageConfigurations.get("sql");
            if (sql != null) {
                storageConfigurations.replace("sql", plaintext(sql, cryptorDelegater));
            }
        }
    }

}
