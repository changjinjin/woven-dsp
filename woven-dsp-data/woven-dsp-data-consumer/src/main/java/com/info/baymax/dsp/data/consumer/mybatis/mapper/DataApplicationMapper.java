package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.DataApplication;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: haijun
 * @Date: 2019/12/16 10:52
 */
@Mapper
public interface DataApplicationMapper extends MyIdableMapper<DataApplication> {
    @Select("select * from dsp_data_application where data_res_id = #{dataResId, jdbcType=BIGINT}")
    List<DataApplication> queryByDataResId(@Param("dataResId") long dataResId);

    @Select({
            "<script>",
            "select",
            "*",
            "from dsp_data_application",
            "where data_res_id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<DataApplication> queryByDataResIds(@Param("ids") List<Long> ids);

    @Delete("delete from dsp_data_application where data_res_id = #{dataResId, jdbcType=BIGINT}")
    int deleteByDataResId(@Param("dataResId") long dataResId);



    @Delete({
            "<script>",
            "delete",
            "from dsp_data_application",
            "where tenant_id = #{tenantId, jdbcType=BIGINT} and  data_res_id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int deleteByDataResIds(@Param("tenantId") Long tenantId,@Param("ids") List<Long> ids);

}
