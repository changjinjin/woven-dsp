package com.merce.woven.dsp.data.sys.crypto.check;

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
	public boolean check(PasswordData passwordData) throws PasswordCheckException {
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
