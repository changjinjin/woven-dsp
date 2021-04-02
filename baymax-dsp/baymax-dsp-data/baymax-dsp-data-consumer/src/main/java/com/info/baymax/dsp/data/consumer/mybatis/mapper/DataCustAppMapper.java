package com.info.baymax.dsp.data.consumer.mybatis.mapper;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.consumer.entity.DataCustApp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface DataCustAppMapper extends MyIdableMapper<DataCustApp> {

    @Select("select access_ip from dsp_dc_appconfig where cust_id = #{custId,jdbcType=VARCHAR}")
    List<String> selectAccessIp(@Param("custId") String custId);
}
