package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataService;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
    @Property(name = "cacheType", value = "${cacheType}")})
public interface DataServiceMapper extends MyIdableMapper<DataService> {

    @Select("select * from dsp_data_service where cust_id = #{dspCustomerId,jdbcType=VARCHAR} and service_id = #{serviceId,jdbcType=VARCHAR}")
    DataService selectEntityByCustIdAndSourceId(@Param("dspCustomerId") String dspCustomerId, @Param("serviceId") Long serviceId);

    @Select("select * from dsp_data_service where name = #{name,jdbcType=VARCHAR}")
    DataService selectEntityByName(@Param("name") String name);

    @Select("select * from dsp_data_service where cust_id = #{dspCustomerId,jdbcType=VARCHAR} and type = #{type,jdbcType=INTEGER}")
    List<DataService> selectByCustIdAndType(@Param("dspCustomerId") String custId, @Param("type") int type);
}
