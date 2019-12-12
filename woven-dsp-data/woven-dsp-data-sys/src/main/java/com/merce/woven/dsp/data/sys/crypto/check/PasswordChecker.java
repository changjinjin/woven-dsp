package com.merce.woven.dsp.data.sys.crypto.check;

import org.passay.PasswordData;

/**
 * 密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public interface PasswordChecker {

	/**
	 * 密码检查
	 *
	 * @param passwordData
	 *            密码元数据
	 * @return 是否成功
	 * @throws PasswordCheckException
	 */
	boolean check(PasswordData passwordData) throws PasswordCheckException;

}
