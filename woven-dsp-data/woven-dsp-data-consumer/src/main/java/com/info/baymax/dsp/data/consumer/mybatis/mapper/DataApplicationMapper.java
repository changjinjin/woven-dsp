package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.mybatis.mapper.delete.DeleteListByPrimaryKeyMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:52
 */
@Mapper
public interface DataApplicationMapper extends MyBaseMapper,DeleteListByPrimaryKeyMapper  {

}
