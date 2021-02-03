package com.info.baymax.common.validation.passay.check;

import com.info.baymax.common.validation.config.PassayProperties;
import lombok.Getter;
import lombok.Setter;
import org.passay.PasswordData;
import org.passay.Rule;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 简单模式密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
@Setter
@Getter
public class SimplePasswordChecker implements PasswordChecker {

    private PassayProperties passayProperties;

    public SimplePasswordChecker() {
        this.passayProperties = new PassayProperties();
    }

    public SimplePasswordChecker(PassayProperties passayProperties) {
        Assert.notNull(passayProperties, "Parameter 'passayProperties' can not be null !");
        this.passayProperties = passayProperties;
    }

    @Override
    public List<Rule> getRules(PasswordData passwordData) {
        return passayProperties.getPassayRules(passwordData);
    }

}
