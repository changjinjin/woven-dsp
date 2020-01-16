package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import java.util.List;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.info.baymax.dsp.data.dataset.entity.core.Schema;

@Mapper
public interface SchemaMapper extends MyIdableMapper<Schema> {

	@Select("select * from merce_schema d  left join merce_data_resource_ref t on t.instance_id =d.id WHERE t.id is null and d.schema_mode != 'builtin'")
	List<Schema> findSchemaByNotResoure();

	@Select("select d.* from merce_schema d, merce_data_resource_ref r where d.id = r.instance_id and d.name=#{name} and r.path=#{path} and d.tenant_id=#{tenantId} and d.owner=#{owner}")
	List<Schema> findSchemaByNameAndPath(@Param("tenantId")String tenantId, @Param("owner")String owner,@Param("name") String name, @Param("path")String path);
}
