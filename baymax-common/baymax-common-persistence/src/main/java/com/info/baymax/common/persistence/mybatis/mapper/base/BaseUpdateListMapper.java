package com.info.baymax.common.persistence.mybatis.mapper.base;

import com.info.baymax.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 批量更新接口，该接口类适用于存在主键的数据表，更新记录时传入的数据记录需要存在主键值.
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:02
 */
@RegisterMapper
public interface BaseUpdateListMapper<T>
    extends//
    UpdateListByPrimaryKeyMapper<T>, //
    UpdateListByPrimaryKeySelectiveMapper<T>//
{

}
