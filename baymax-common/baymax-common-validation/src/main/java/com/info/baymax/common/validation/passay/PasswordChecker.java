package com.info.baymax.common.validation.passay;

import com.info.baymax.common.core.exception.BizException;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.validation.config.PassayProperties;
import lombok.Getter;
import org.passay.PasswordData;
import org.passay.PasswordData.HistoricalReference;
import org.passay.PasswordData.SourceReference;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public class PasswordChecker implements Rule {
    private final PassayProperties passayProperties;
    @Getter
    private List<String> messages;

    public PasswordChecker() {
        this.passayProperties = new PassayProperties();
    }

    public PasswordChecker(PassayProperties passayProperties) {
        Assert.notNull(passayProperties, "Parameter 'passayProperties' can not be null !");
        this.passayProperties = passayProperties;
    }

    /**
     * 获取规则列表
     *
     * @return 规则列表
     */
    private List<Rule> getRules(PasswordData passwordData) {
        return passayProperties.getPassayRules(passwordData);
    }

    public RuleResult check(String username, String oldPwd, String newPwd) {
        PasswordData passwordData = new PasswordData(SaasContext.getCurrentUsername(), newPwd);
        passwordData.setPasswordReferences(new SourceReference("source", oldPwd), new HistoricalReference(newPwd));
        RuleResult result = validate(passwordData);
        if (!result.isValid()) {
            throw new BizException(ErrType.BAD_REQUEST, String.join("\n", getMessages()));
        }
        return result;
    }

    @Override
    public RuleResult validate(PasswordData passwordData) {
        clear();
        PasswordValidator validator = getValidator(passwordData);
        RuleResult ruleResult = validator.validate(passwordData);
        if (!ruleResult.isValid()) {
            messages = validator.getMessages(ruleResult);
        }
        return ruleResult;
    }

    private PasswordValidator getValidator(PasswordData passwordData) {
        return new PasswordValidator(new PassayPropertiesMessageResolver(PassayMessageSource.getAccessor()),
            getRules(passwordData));
    }

    private void clear() {
        this.messages = null;
    }
}
