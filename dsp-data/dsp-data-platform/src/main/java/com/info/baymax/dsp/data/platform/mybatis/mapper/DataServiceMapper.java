package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DataServiceMapper extends MyIdableMapper<DataService> {

    @Update("update dsp_data_service set is_running = 1 where type = #{type, jdbcType=INTEGER} and status = #{status, jdbcType=INTEGER} and is_running = #{isRunning, jdbcType=INTEGER}")
    void updateSpecialDataServiceToRunning(@Param("type") int type, @Param("status") int status, @Param("isRunning") int isRunning);

    @Update("update dsp_data_service set is_running = #{isRunning, jdbcType=INTEGER} where id = #{id, jdbcType=BIGINT}")
    void updateDataServiceRunningStatus(@Param("id") Long id, @Param("isRunning") Integer isRunning);

    @Update("update dsp_data_service set status = #{status, jdbcType=INTEGER} where id = #{id, jdbcType=BIGINT}")
    void updateDataServiceStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("update dsp_data_service set is_running = 0 where status = 1 and (is_running = 1 or schedule_type = 'cron')")
    void recoverDataService();
}
