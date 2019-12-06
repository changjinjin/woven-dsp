package com.merce.woven.common.mybatis.mapper.base;

import com.merce.woven.common.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.merce.woven.common.mybatis.mapper.delete.DeleteListByPrimaryKeyMapper;
import com.merce.woven.common.mybatis.mapper.delete.DeleteListMapper;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 批量删除接口
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:00
 * @param <T>
 */
@RegisterMapper
public interface BaseDeleteListMapper<T> extends//
		DeleteListMapper<T>, //
		DeleteListByPrimaryKeyMapper<T>, //
		DeleteByPrimaryKeysMapper<T>//
{

}
