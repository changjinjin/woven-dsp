package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
    @Property(name = "cacheType", value = "${cacheType}")})
public interface DataResourceMapper extends MyIdableMapper<DataResource> {

    @Select({
            "<script>",
                "select r.* from dsp_data_resource r join dsp_data_service s on r.id = s.data_res_id where s.id in ",
                "<foreach collection='ids' item='item' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>"
    })
    List<DataResource> selectDataResourceListByIds(@Param("ids") List<Long> ids);

    @Select("select * from dsp_data_resource where name = #{name,jdbcType=VARCHAR}")
    DataResource selectEntityByName(@Param("name") String name);
}
