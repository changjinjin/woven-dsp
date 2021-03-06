package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.core.enums.types.YesNoType;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.persistence.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.validation.passay.PasswordChecker;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.entity.security.UserRoleRef;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.RoleMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.UserMapper;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.UserRoleRefMapper;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl extends EntityClassServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleRefMapper userRoleRefMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordChecker passwordChecker;

    @Override
    public MyIdableMapper<User> getMyIdableMapper() {
        return userMapper;
    }

    @Cacheable(cacheNames = CacheNames.CACHE_SECURITY, key = "'security_cache_user_'+#tenantId+'_'+#loginId", unless = "#result==null")
    @Override
    public User findByTenantAndUsername(String tenantId, String loginId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", tenantId);
        params.put("loginId", loginId);
        return userMapper.selectOneWithRoles(params);
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Transactional
    @Override
    public User save(User t) {
        if (existsByTenantIdAndName(SaasContext.getCurrentTenantId(), t.getUsername())) {
            throw new ServiceException(ErrType.ENTITY_EXIST, "????????????????????????");
        }
        LocalDateTime now = LocalDateTime.now();
        t.setPwdExpiredTime(Date.from(now.plusMonths(3).atZone(ZoneId.systemDefault()).toInstant()));
        t.setAccountExpiredTime(Date.from(now.plusMonths(6).atZone(ZoneId.systemDefault()).toInstant()));
        if (t.getAdmin() == null) {
            t.setAdmin(YesNoType.NO.getValue());
        }
        t.setEnabled(YesNoType.YES.getValue());

        // ??????????????????client???????????????
        String clientIds = t.getClientIds();
        if (StringUtils.isEmpty(clientIds)) {
            t.setClientIds("dsp");
        }

        String password = t.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException(ErrType.BAD_REQUEST, "user password must be not null");
        }
        t.setPassword(passwordEncoder.encode(password));

        // ??????????????????
        insertSelective(t);

        // ???????????????????????????
        updateUserRoleRefs(t, t.getRoles());
        return t;
    }

    private void updateUserRoleRefs(User t, List<Role> roles) {
        // ???????????????????????????
        if (ICollections.hasElements(roles)) {
            // ?????????????????????
            userRoleRefMapper.deleteByUserId(t.getId());
            // ?????????????????????
            for (Role r : roles) {
                if (r.getId() == null) {
                    userRoleRefMapper.insertSelective(new UserRoleRef(t.getId(), r.getId()));
                }
            }
        }
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public User update(User t) {
        // ??????????????????
        updateByPrimaryKeySelective(t);
        // ???????????????????????????
        updateUserRoleRefs(t, t.getRoles());
        return t;
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int resetPwd(String[] ids, String initPwd) {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                User t = new User();
                t.setId(id);
                t.setPassword(passwordEncoder.encode(initPwd));
                updateByPrimaryKeySelective(t);
            }
            return ids.length;
        }
        return 1;
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int resetStatus(List<User> list) {
        if (ICollections.hasElements(list)) {
            for (User user : list) {
                updateByPrimaryKeySelective(user);
            }
            return list.size();
        }
        return 1;
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int changePwd(String oldPass, String newPass) {
        User merceUser = selectByPrimaryKey(SaasContext.getCurrentUserId());
        if (merceUser == null) {
            throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "???????????????????????????????????????");
        }

        // ??????????????????
        if (!passwordEncoder.matches(oldPass, merceUser.getPassword())) {
            throw new BadCredentialsException("???????????????????????????????????????");
        }

        // ??????????????????
        passwordChecker.check(SaasContext.getCurrentUsername(), oldPass, newPass);

        merceUser.setPassword(passwordEncoder.encode(newPass));
        merceUser.setPwdExpiredTime(
            Date.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()).toInstant()));
        updateByPrimaryKeySelective(merceUser);
        return 1;
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int deleteById(String id) {
        User user = selectByPrimaryKey(id);
        if (YesNoType.YES.equalsTo(user.getEnabled())) {
            throw new ServiceException(ErrType.ENTITY_DELETE_ERROR, "????????????????????????????????????");
        }
        return deleteByPrimaryKey(id);
    }

    @CacheEvict(cacheNames = CacheNames.CACHE_SECURITY, allEntries = true)
    @Override
    public int deleteByIds(String[] ids) {
        List<User> list = findByIds(SaasContext.getCurrentTenantId(), ids);
        for (User user : list) {
            if (YesNoType.YES.equalsTo(user.getEnabled())) {
                throw new ServiceException(ErrType.ENTITY_DELETE_ERROR, "????????????????????????????????????");
            }
        }
        deleteByPrimaryKeys(ids);
        return ids.length;
    }

    @Override
    public IPage<User> selectPage(ExampleQuery query) {
        IPage<User> page = UserService.super.selectPage(query);
        if (page != null) {
            fetchUsersRoles(page.getList());
        }
        return page;
    }

    /**
     * ?????????????????????????????????
     *
     * @param list ????????????
     */
    private void fetchUsersRoles(List<User> list) {
        if (ICollections.hasElements(list)) {
            for (User u : list) {
                fetchUserRoles(u);
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param u ??????
     */
    private void fetchUserRoles(User u) {
        if (u != null) {
            u.setRoles(roleMapper.selectOnlyRoleByUserId(u.getId()));
        }
    }

}
