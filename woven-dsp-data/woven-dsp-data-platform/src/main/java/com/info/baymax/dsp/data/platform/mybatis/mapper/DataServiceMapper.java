package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DataServiceMapper extends MyIdableMapper<DataService> {

    @Select("select * from dsp_data_service where type = #{type, jdbcType=INTEGER} and status = #{status, jdbcType=INTEGER} and is_running = #{isRunning, jdbcType=INTEGER} for update")
    List<DataService> querySpecialDataService(@Param("type") int type, @Param("status") int status, @Param("isRunning") int isRunning);

    @Update("update dsp_data_service set is_running = 1 where type = #{type, jdbcType=INTEGER} and status = #{status, jdbcType=INTEGER} and is_running = #{isRunning, jdbcType=INTEGER}")
    void updateDataServiceToRunning(@Param("type") int type, @Param("status") int status, @Param("isRunning") int isRunning);

    @Update("update dsp_data_service set is_running = 1 where id = #{id, jdbcType=BIGINT}")
    void updateDataServiceToRunning(@Param("id") Long id);


    @Update("update dsp_data_service set is_running = 0 where status = 1 and is_running = 1")
    void recoverDataService();

    @Update("update dsp_data_service set status = 3 where schedule_type = 'once' and executed_times > 0")
    void updateFinishedDataService();
}
