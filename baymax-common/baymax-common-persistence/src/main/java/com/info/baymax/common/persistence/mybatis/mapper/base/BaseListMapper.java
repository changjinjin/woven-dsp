package com.info.baymax.common.persistence.mybatis.mapper.base;

import com.info.baymax.common.persistence.mybatis.mapper.insert.InsertListWithPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.insert.ReplaceListWithPrimaryKeyMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 批量操作汇总接口
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:00
 */
@RegisterMapper
public interface BaseListMapper<T>
    extends //
    MySqlMapper<T>, //
    InsertListWithPrimaryKeyMapper<T>, //
    ReplaceListWithPrimaryKeyMapper<T>, //
    BaseDeleteListMapper<T>, //
    BaseUpdateListMapper<T> //
{

}
