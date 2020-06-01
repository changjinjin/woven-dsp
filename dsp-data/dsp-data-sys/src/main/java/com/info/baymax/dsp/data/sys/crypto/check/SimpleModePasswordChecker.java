package com.info.baymax.dsp.data.sys.crypto.check;

import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import org.apache.commons.lang3.StringUtils;
import org.passay.PasswordData;

/**
 * 简单模式密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public class SimpleModePasswordChecker implements PasswordChecker {

	@Override
	public boolean supports(PwdMode pwdMode) {
		return PwdMode.SIMPLE.equals(pwdMode);
	}

	@Override
	public boolean check(PwdMode pwdMode, PasswordData passwordData) throws PasswordCheckException {
		String password = passwordData.getPassword();
		if (StringUtils.isEmpty(password)) {
			throw new PasswordCheckException("密码不能为空");
		}
		if (password.length() < 6) {
			throw new PasswordCheckException("密码长度不能小于6位");
		}
		return true;
	}

}
