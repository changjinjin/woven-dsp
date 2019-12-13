package com.info.baymax.common.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.aggregation.AggregationMapper;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.mybatis.mapper.delete.DeleteListMapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.additional.update.differ.UpdateByDifferMapper;
import tk.mybatis.mapper.additional.update.force.UpdateByPrimaryKeySelectiveForceMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.base.delete.DeleteMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.rowbounds.SelectRowBoundsMapper;


/**
 * 基本增删改查操作接口
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:07
 */
@RegisterMapper
public interface MyBaseMapper<T> extends //
    InsertMapper<T>, //
    InsertSelectiveMapper<T>, //
    InsertListMapper<T>, //
    DeleteMapper<T>, //
    DeleteListMapper<T>, //
    SelectOneMapper<T>, //
    SelectMapper<T>, //
    SelectAllMapper<T>, //
    SelectRowBoundsMapper<T>, //
    SelectCountMapper<T>, //
    UpdateByDifferMapper<T>, //
    UpdateByPrimaryKeySelectiveForceMapper<T>, //
    BaseExampleMapper<T>,//
    AggregationMapper<T>

{
}
