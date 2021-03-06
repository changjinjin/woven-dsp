package com.info.baymax.dsp.data.sys.service.security.impl;

import com.google.common.collect.Lists;
import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.*;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RoleMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RolePermissionRefMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RoleResourceRefMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.UserRoleRefMapper;
import com.info.baymax.dsp.data.sys.service.security.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class RoleServiceImpl extends EntityClassServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleRefMapper userRoleRefMapper;
    @Autowired
    private RolePermissionRefMapper rolePermissionRefMapper;
    @Autowired
    private RoleResourceRefMapper roleResourceRefMapper;

    @Override
    public MyIdableMapper<Role> getMyIdableMapper() {
        return roleMapper;
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public Role save(Role t) {
        if (existsByTenantIdAndName(SaasContext.getCurrentTenantId(), t.getName())) {
            throw new ServiceException(ErrType.ENTITY_EXIST, "????????????????????????");
        }
        // ??????????????????
        t.setEnabled(YesNoType.YES.getValue());
        insertSelective(t);

        // ???????????????????????????
        updateRolePermissionRefs(t, t.getPermissions());
        return t;
    }

    private void updateRolePermissionRefs(Role t, Set<Permission> permissions) {
        rolePermissionRefMapper.deleteByRoleId(t.getId());
        if (ICollections.hasElements(permissions)) {
            for (Permission p : permissions) {
                rolePermissionRefMapper.insertSelective(new RolePermissionRef(t.getId(), p.getId(), p.getHalfSelect()));
            }
        }
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public Role update(Role t) {
        if (t.getId() == null) {
            t.setId(null);
            save(t);
        } else {
            // ??????????????????
            updateByPrimaryKeySelective(t);
            // ???????????????????????????
            updateRolePermissionRefs(t, t.getPermissions());
        }
        return t;
    }

    private void updateRoleResourceRefs(Role t, List<RoleResourceRefGroup> rrrfGroups) {
        roleResourceRefMapper.deleteByRoleId(t.getId());
        if (ICollections.hasElements(rrrfGroups)) {
            for (RoleResourceRefGroup rrrg : rrrfGroups) {
                List<RoleResourceRef> rrrs = rrrg.getRrrs();
                for (RoleResourceRef rrr : rrrs) {
                    rrr.setRoleId(t.getId());
                    rrr.setResType(rrrg.getResType());
                    roleResourceRefMapper.insertSelective(rrr);
                }
            }
        }
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public Role updateRrrs(Role t) {
        updateRoleResourceRefs(t, t.getRrrfGroups());
        return t;
    }

    @Override
    public int deleteById(String id) {
        // ????????????????????????????????????
        userRoleRefMapper.deleteByRoleId(id);
        // ????????????????????????????????????
        rolePermissionRefMapper.deleteByRoleId(id);
        // ?????????????????????????????????????????????
        roleResourceRefMapper.deleteByRoleId(id);
        // ??????????????????
        return deleteByPrimaryKey(id);
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int deleteByIds(List<String> ids) {
        return RoleService.super.deleteByIds(ids);
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int resetStatus(List<Role> list) {
        if (ICollections.hasElements(list)) {
            updateListByPrimaryKeySelective(list);
            return list.size();
        }
        return 1;
    }

    @Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
    @Override
    public Role selectWithPermissionsAndResourcesRefsById(String id) {
        Role t = roleMapper.selectWithPermissionsById(id);
        if (t != null) {
            List<RoleResourceRef> list = roleResourceRefMapper.select(new RoleResourceRef(t.getId()));
            if (ICollections.hasElements(list)) {
                Map<String, List<RoleResourceRef>> refGroup = list.stream()
                    .collect(Collectors.groupingBy(RoleResourceRef::getResType));
                List<RoleResourceRefGroup> rrrfGroups = Lists.newArrayList();
                refGroup.forEach((k, v) -> {
                    rrrfGroups.add(new RoleResourceRefGroup(k, v));
                });
                t.setRrrfGroups(rrrfGroups);
            }
        }
        return t;
    }
}
