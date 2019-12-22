package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:52
 */
@Mapper
public interface DataApplicationMapper extends MyIdableMapper<DataApplication> {

    @Update("update dsp_data_application set status = #{status, jdbcType=INTEGER} where id = #{id, jdbcType=BIGINT}")
    void updateDataApplicationStatus(@Param("id") Long id, @Param("status") Integer status);
}
