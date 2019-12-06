package com.merce.woven.common.mybatis.mapper.base;

import com.merce.woven.common.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.merce.woven.common.mybatis.mapper.select.SelectByPrimaryKeysMapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;

/**
 * 根据主键集合批量操作接口
 *
 * @author jingwei.yang
 * @date 2019-05-28 14:59
 */
@RegisterMapper
public interface BaseByPrimaryKeysMapper<T> extends//
		IdsMapper<T>, //
		DeleteByPrimaryKeysMapper<T>, //
		SelectByPrimaryKeysMapper<T> //
{

}
