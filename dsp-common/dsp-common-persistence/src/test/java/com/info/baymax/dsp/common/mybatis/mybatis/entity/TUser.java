package com.info.baymax.dsp.common.mybatis.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jingwei.yang
 * @date 2019-06-28 12:15:40
 */
@ApiModel
@Table(name = "t_user")
public class TUser implements Serializable {
    private static final long serialVersionUID = 1844123293605109217L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @KeySql(genId = UuidGenId.class)
    private Long id;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    @ColumnType(column = "REAL_NAME")
    private String realName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 生日
     */
    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    /**
     * 性别：0-男，1-女
     */
    @ApiModelProperty("性别：0-男，1-女")
    private Byte gender;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用
     */
    @ApiModelProperty("用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用")
    private Byte status;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取账号
     *
     * @return username - 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置账号
     *
     * @param username 账号
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取性别：0-男，1-女
     *
     * @return gender - 性别：0-男，1-女
     */
    public Byte getGender() {
        return gender;
    }

    /**
     * 设置性别：0-男，1-女
     *
     * @param gender 性别：0-男，1-女
     */
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用
     *
     * @return status - 用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用
     *
     * @param status 用户状态：0-未启用,1-已启用,2-已锁定,3-已禁用
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((birth == null) ? 0 : birth.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((realName == null) ? 0 : realName.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TUser other = (TUser) obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (birth == null) {
            if (other.birth != null)
                return false;
        } else if (!birth.equals(other.birth))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (mobile == null) {
            if (other.mobile != null)
                return false;
        } else if (!mobile.equals(other.mobile))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (realName == null) {
            if (other.realName != null)
                return false;
        } else if (!realName.equals(other.realName))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TUser [id=" + id + ", username=" + username + ", realName=" + realName + ", password=" + password
            + ", birth=" + birth + ", gender=" + gender + ", mobile=" + mobile + ", email=" + email + ", status="
            + status + ", address=" + address + "]";
    }

}