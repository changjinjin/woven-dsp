package com.merce.woven.common.message.result;

/**
 * 错误信息
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:53:43
 */
public interface ErrMsg {

	/**
	 * 获取消息编码
	 *
	 * @return 消息编码
	 */
	Integer getStatus();

	/**
	 * 获取消息
	 *
	 * @return 消息内容
	 */
	String getMessage();

}
