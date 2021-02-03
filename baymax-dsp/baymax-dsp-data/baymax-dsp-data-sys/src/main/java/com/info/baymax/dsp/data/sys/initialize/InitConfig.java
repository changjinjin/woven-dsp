package com.info.baymax.dsp.data.sys.initialize;

import lombok.Data;

/**
 * 说明：模板配置信息. <br>
 *
 * @author jingwei.yang
 * @date 2018年6月15日 上午9:22:09
 */
@Data
public class InitConfig {

    /**
     * 是否开启系统初始化
     */
    private boolean enabled = false;

    /**
     * 初始化账户
     */
    private String username = "admin";

    /**
     * 初始化密码
     */
    private String password = "123456";

    /**
     * 系统权限初始化文件
     */
    private String permsFile = "classpath:init/perms/roots.xml";
}
