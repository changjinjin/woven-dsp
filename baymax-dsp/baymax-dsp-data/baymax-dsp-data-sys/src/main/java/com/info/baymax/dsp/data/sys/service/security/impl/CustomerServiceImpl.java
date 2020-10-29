package com.info.baymax.dsp.data.sys.service.security.impl;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.queryapi.exception.ServiceException;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.crypto.check.CompositePasswordChecker;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.mybatis.mapper.security.CustomerMapper;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import org.passay.PasswordData;
import org.passay.PasswordData.SourceReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class CustomerServiceImpl extends EntityClassServiceImpl<Customer> implements CustomerService {

    @Autowired
    private CustomerMapper consumerMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public MyIdableMapper<Customer> getMyIdableMapper() {
        return consumerMapper;
    }

    @Override
    public Customer findByTenantAndUsername(String tenantId, String username) {
        return selectOne(ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andEqualTo("tenantId", tenantId).andEqualTo("username", username)));
    }

    @Override
    public Customer save(Customer t) {
        String username = t.getUsername();
        Customer exist = findByTenantAndUsername(SaasContext.getCurrentTenantId(), username);
        if (exist != null) {
            throw new ServiceException(ErrType.ENTITY_EXIST, "同名的消费者信息已经存在");
        }

        t.setEnabled(YesNoType.YES.getValue());
        t.setPassword(passwordEncoder.encode(t.getPassword()));
        return CustomerService.super.save(t);
    }

    @Override
    public Customer update(Customer t) {
        // 不能修改账号和密码
        t.setUsername(null);
        t.setPassword(null);
        return CustomerService.super.update(t);
    }

    @Override
    public int resetPwd(String[] ids, String initPwd) {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                Customer t = new Customer();
                t.setId(id);
                t.setPassword(passwordEncoder.encode(initPwd));
                updateByPrimaryKeySelective(t);
            }
            return ids.length;
        }
        return 1;
    }

    @Override
    public int resetStatus(List<Customer> list) {
        if (ICollections.hasElements(list)) {
            for (Customer user : list) {
                updateByPrimaryKeySelective(user);
            }
            return list.size();
        }
        return 1;
    }

    @Override
    public int changePwd(String oldPass, String newPass, PwdMode pwdMode) {
        Customer t = selectByPrimaryKey(SaasContext.getCurrentUserId());
        if (t == null) {
            throw new ServiceException(ErrType.ENTITY_NOT_EXIST, "密码修改失败，用户不存在！");
        }

        // 密码加密匹配
        if (!passwordEncoder.matches(oldPass, t.getPassword())) {
            throw new BadCredentialsException("密码修改失败，原密码错误！");
        }

        // 密码格式检查
        PasswordData passwordData = new PasswordData(SaasContext.getCurrentUsername(), newPass);
        passwordData.setPasswordReferences(new SourceReference(oldPass));
        CompositePasswordChecker passwordChecker = new CompositePasswordChecker();
        passwordChecker.check(pwdMode, passwordData);

        t.setPassword(passwordEncoder.encode(newPass));
        updateByPrimaryKeySelective(t);
        return 1;
    }

}
