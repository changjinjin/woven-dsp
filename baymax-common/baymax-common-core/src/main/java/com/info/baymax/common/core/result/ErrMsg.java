package com.info.baymax.common.core.result;

/**
 * 错误信息
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:53:43
 */
public interface ErrMsg extends ErrResult {

    /**
     * 获取信息字符串编码，用于消息国际化
     *
     * @return 信息字符串编码
     */
    String getCode();

}
