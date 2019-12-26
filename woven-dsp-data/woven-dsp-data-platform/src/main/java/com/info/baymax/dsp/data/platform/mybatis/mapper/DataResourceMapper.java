package com.info.baymax.dsp.data.platform.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DataResourceMapper extends MyIdableMapper<DataResource> {

    @Update({
            "<script>",
            "update",
            "dsp_data_resource",
            "set open_status = 0",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void closeDataResourceByIds(@Param("ids") List<Long> ids);
}
