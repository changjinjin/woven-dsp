package com.info.baymax.dsp.data.sys.crypto.check;

import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import org.passay.PasswordData;

/**
 * 密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public interface PasswordChecker {

    /***
     * 模式适配
     *
     * @param pwdMode 校验模式
     * @return 是否适配
     */
    boolean supports(PwdMode pwdMode);

    /**
     * 密码检查
     *
     * @param pwdMode      校验模式
     * @param passwordData 密码元数据
     * @return 是否成功
     * @throws PasswordCheckException
     */
    boolean check(PwdMode pwdMode, PasswordData passwordData) throws PasswordCheckException;

}
