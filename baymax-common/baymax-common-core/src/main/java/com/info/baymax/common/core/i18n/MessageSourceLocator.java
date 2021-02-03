package com.info.baymax.common.core.i18n;

import java.util.List;

/**
 * 国际化资源定位器
 *
 * @author jingwei.yang
 * @date 2021年2月2日 下午4:48:21
 */
public interface MessageSourceLocator {

    /**
     * 指定本模块中的国际化资源basename，可指定多个，返回数组
     *
     * @return 国际化资源数组
     */
    List<String> baseNames();

}
