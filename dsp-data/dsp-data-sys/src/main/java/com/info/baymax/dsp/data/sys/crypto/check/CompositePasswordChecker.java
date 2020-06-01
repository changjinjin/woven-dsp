package com.info.baymax.dsp.data.sys.crypto.check;

import com.google.common.collect.Lists;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import org.passay.PasswordData;

import java.util.ArrayList;
import java.util.List;

public class CompositePasswordChecker implements PasswordChecker {

    private final List<PasswordChecker> checkers = new ArrayList<PasswordChecker>();

    public CompositePasswordChecker() {
        checkers.add(new SimpleModePasswordChecker());
        checkers.add(new StrictModePasswordChecker());
    }

    @Override
    public boolean supports(PwdMode pwdMode) {
        return true;
    }

    @Override
    public boolean check(PwdMode pwdMode, PasswordData passwordData) throws PasswordCheckException {
        for (PasswordChecker checker : checkers) {
            if (checker.supports(pwdMode)) {
                return checker.check(pwdMode, passwordData);
            }
        }
        throw new PasswordCheckException("No suitable password checker!");
    }

    public void addCheckers(PasswordChecker... checkers) {
        addCheckers(Lists.newArrayList(checkers));
    }

    public void addCheckers(List<PasswordChecker> checkers) {
        if (checkers != null) {
            this.checkers.addAll(checkers);
        }
    }

}
