package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.entity.base.Maintable;
import com.info.baymax.common.jpa.converter.ObjectToStringConverter;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.mybatis.type.clob.GZBase64ClobVsConfigObjectTypeHandler;
import com.info.baymax.dsp.data.dataset.service.resource.ResourceId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@Entity
@Table(name = "merce_dss", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenantId", "name"})}, indexes = {
    @Index(columnList = "lastModifiedTime"), @Index(columnList = "owner"), @Index(columnList = "resourceId")})
@Comment("数据源信息表")
public class DataSource extends Maintable implements ResourceId, CryptoBean {
    private static final long serialVersionUID = 20947213660517221L;

    @Transient
    private final String tableName = "merce_dss";

    @ApiModelProperty("数据源类型：DB,HTTP,FTP,ES,MONGODB,SOCKET等")
    @Comment("数据源类型：DB,HTTP,FTP,ES,MONGODB,SOCKET等")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String type;

    @ApiModelProperty("资源目录路径")
    @Transient
    private String path;

    @ApiModelProperty("配置参数")
    @Comment("配置参数")
    @Lob
    @Convert(converter = ObjectToStringConverter.class)
    @ColumnType(jdbcType = JdbcType.CLOB, typeHandler = GZBase64ClobVsConfigObjectTypeHandler.class)
    private ConfigObject attributes;

    @ApiModelProperty("所属项目信息")
    @Transient
    private ProjectEntity projectEntity;

    @ApiModelProperty("所属资源目录信息")
    @Transient
    private ResourceDesc resource;

    @ApiModelProperty("资源目录ID")
    @Comment("资源目录ID")
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private String resourceId;

    public DataSource() {
    }

    public DataSource(String type, ConfigObject attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    // 获取resourceId,如果为空则从resource中获取
    public String getResourceId() {
        if (resourceId == null && resource != null) {
            resourceId = resource.getId();
        }
        return resourceId;
    }

    @Override
    public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (attributes != null || !attributes.isEmpty()) {
            Object password = attributes.get("password");
            if (password != null) {
                attributes.replace("password",
                    ciphertext(password.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
            }
        }
    }

    @Override
    public void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (attributes != null || !attributes.isEmpty()) {
            Object password = attributes.get("password");
            if (password != null) {
                attributes.replace("password",
                    plaintext(password.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
            }
        }
    }

}
