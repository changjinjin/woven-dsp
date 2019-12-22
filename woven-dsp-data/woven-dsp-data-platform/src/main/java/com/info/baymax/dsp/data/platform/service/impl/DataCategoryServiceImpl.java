package com.info.baymax.dsp.data.platform.service.impl;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.platform.entity.DataCategory;
import com.info.baymax.dsp.data.platform.mybatis.mapper.DataCategoryMapper;
import com.info.baymax.dsp.data.platform.service.DataCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class DataCategoryServiceImpl extends EntityClassServiceImpl<DataCategory> implements DataCategoryService {

    @Autowired
    private DataCategoryMapper dataCategoryMapper;

    @Override
    public MyIdableMapper<DataCategory> getMyIdableMapper() {
        return dataCategoryMapper;
    }

    @Override
    public DataCategory save(DataCategory t) {
        if (t.getParentId() == null) {
            t.setParentId(0L);
        }
        // 检查name属性
        checkName(t);

        // 设置order属性
        Long parentId = t.getParentId();
        t.setOrder(dataCategoryMapper.selectMaxOrder(parentId));

        // 设置path属性
        DataCategory parent = selectByPrimaryKey(parentId);
        if (parent == null) {
            t.setPath(t.getName() + ";");
        } else {
            t.setPath(parent.getPath() + t.getName() + ";");
        }

        // 启用
        t.setEnabled(YesNoType.YES.getValue());
        return DataCategoryService.super.save(t);
    }

    private void checkName(DataCategory t) {
        if (t.getId() == null) {
            DataCategory record = new DataCategory();
            record.setParentId(t.getParentId());
            record.setName(t.getName());
            if (exists(record)) {
                throw new ServiceException(ErrType.ENTITY_EXIST, "同层级相同名称的目录已经存在。");
            }
        } else {
            ExampleQuery existQuery = ExampleQuery.builder(getEntityClass())//
                .fieldGroup()//
                .andEqualTo("name", t.getName())//
                .andNotEqualTo("id", t.getId())//
                .end();
            if (exists(existQuery)) {
                throw new ServiceException(ErrType.ENTITY_EXIST, "同层级相同名称的目录已经存在。");
            }
        }
    }

    @Override
    public DataCategory update(DataCategory t) {
        checkName(t);
        return DataCategoryService.super.update(t);
    }

    @Override
    public int sort(List<DataCategory> list) {
        int i = 1;
        for (DataCategory t : list) {
            t.setOrder(i++);
            updateByPrimaryKeySelective(t);
        }
        return i - 1;
    }

    @Override
    public int move(Long[] ids, Long destId) {
        DataCategory record = null;
        for (Long id : ids) {
            record = new DataCategory();
            record.setId(id);
            record.setParentId(destId);
            updateByPrimaryKeySelective(record);
        }
        return ids.length;
    }

    @Override
    public int deleteById(Long id) {
        DataCategory root = selectByPrimaryKey(id);
        if (root != null) {
            List<DataCategory> list = selectList(ExampleQuery.builder(getEntityClass()).selectProperties("id")
                .fieldGroup().andEqualTo("tenantId", SaasContext.getCurrentTenantId())
                .andRightLike("path", root.getPath()).end());
            if (ICollections.hasElements(list)) {
                List<Long> ids = list.stream().map(t -> t.getId()).collect(Collectors.toList());
                return deleteByPrimaryKeys(ids);
            }
        }
        return 0;
    }

    @Override
    public List<DataCategory> tree(Long rootId) {
        Map<String, Object> extParams = new HashMap<String, Object>();
        extParams.put("tenantId", SaasContext.getCurrentTenantId());
        return selectChilrenTree(rootId, extParams);
    }

}