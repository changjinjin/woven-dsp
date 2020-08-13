package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.dsp.data.dataset.entity.core.Schema;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.SchemaMapper;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.service.resource.QueryObjectByResourceOrProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaServiceImpl extends QueryObjectByResourceOrProjectServiceImpl<Schema> implements SchemaService {

    @Autowired
    private SchemaMapper schemaMapper;

    @Override
    public MyIdableMapper<Schema> getMyIdableMapper() {
        return schemaMapper;
    }

    @Override
    public List<Schema> findSchemaByNotResoure() {
        return schemaMapper.findSchemaByNotResoure();
    }

    @Override
    public List<Schema> findSchemaByNameAndPath(String tenantId, String owner, String name, String path) {
        return schemaMapper.findSchemaByNameAndPath(tenantId, owner, name, path);
    }

    @Override
    public List<Schema> findSchemaByOId(String tenant, String oid) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenant).andEqualTo("oid", oid))//
            .orderByDesc("version").orderByDesc("lastModifiedTime"));
    }

    @Override
    public List<Schema> findSchemaByOIdAndVersion(String tenant, String oid, Integer version) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenant).andEqualTo("oid", oid)
                .andEqualTo("version", version))//
            .orderByDesc("lastModifiedTime"));
    }

    @Override
    public Schema findOneByName(String tenant, String name) {
        List<Schema> list = selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenant).andEqualTo("name", name))//
            .orderByDesc("version").orderByDesc("lastModifiedTime"));
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
