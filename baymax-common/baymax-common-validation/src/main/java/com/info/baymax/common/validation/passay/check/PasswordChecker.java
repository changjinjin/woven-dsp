package com.info.baymax.common.validation.passay.check;

import com.info.baymax.common.validation.passay.PassayMessageSource;
import com.info.baymax.common.validation.passay.PassayPropertiesMessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

import java.util.List;

/**
 * 密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public interface PasswordChecker extends Rule {

    /**
     * 获取规则列表
     *
     * @return 规则列表
     */
    List<Rule> getRules(PasswordData passwordData);

    @Override
    default RuleResult validate(PasswordData passwordData) {
        return getValidator(passwordData).validate(passwordData);
    }

    default PasswordValidator getValidator(PasswordData passwordData) {
        return new PasswordValidator(new PassayPropertiesMessageResolver(PassayMessageSource.getAccessor()),
            getRules(passwordData));
    }

}
