package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.User;

@Mapper
public interface UserMapper extends MyIdableMapper<User> {

    /**
     * 根据ID查询用户信息一并查出角色信息
     *
     * @param params 查询参数
     * @return 用户信息包含级联的角色信息和权限信息
     */
    User selectOneWithRoles(Map<String, Object> params);
}
