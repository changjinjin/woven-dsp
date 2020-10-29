package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.entity.security.PermOperationRef;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RestOperationMapper;
import com.info.baymax.dsp.data.sys.service.security.PermOperationRefService;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestOperationServiceImpl extends EntityClassServiceImpl<RestOperation> implements RestOperationService {
    @Autowired
    private RestOperationMapper restOperationMapper;

    @Autowired
    private PermOperationRefService permOperationRefService;

    @Override
    public MyIdableMapper<RestOperation> getMyIdableMapper() {
        return restOperationMapper;
    }

    @Override
    public int initRestOperations(String serviceName, List<RestOperation> list) {
        if (ICollections.hasElements(list)) {
            String[] reservedIds = list.stream().map(t -> t.getId()).toArray(String[]::new);
            clear(serviceName, reservedIds);
            for (RestOperation t : list) {
                saveOrUpdate(t);
            }
            return list.size();
        }
        return 0;
    }

    /**
     * 清理掉已经删除的接口信息
     *
     * @param serviceName 服务名称
     * @param reservedIds 需要保留的数据的ID列表（ID对于每一个RestOperation是唯一且不可变的）
     */
    private void clear(String serviceName, String[] reservedIds) {
        permOperationRefService.delete(ExampleQuery.builder(PermOperationRef.class)
            .fieldGroup(FieldGroup.builder().andNotIn("operationId", reservedIds)));
        delete(ExampleQuery.builder(RestOperation.class)
            .fieldGroup(FieldGroup.builder().andEqualTo("serviceName", serviceName).andNotIn("id", reservedIds)));
    }
}
