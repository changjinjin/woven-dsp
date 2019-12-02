package com.merce.woven.dsp.common.constants;

/**
 * 公共常量
 * 
 * @author yjw@jusfoun.com
 * @date 2018-12-17 12:57:29
 */
public class Constants {

	/**
	 * token请求头名称
	 */
	public static final String REQ_HEADER = "Authorization";

	/**
	 * token分割符
	 */
	public static final String TOKEN_SPLIT = "Bearer ";

	/**
	 * jwt签名
	 */
	public static final String SIGN_KEY = "PIG";
	/**
	 * 删除
	 */
	public static final String STATUS_DEL = "1";
	/**
	 * 正常
	 */
	public static final String STATUS_NORMAL = "0";

	/**
	 * 锁定
	 */
	public static final String STATUS_LOCK = "9";

	/**
	 * 菜单
	 */
	public static final String MENU = "0";

	/**
	 * 按钮
	 */
	public static final String BUTTON = "1";

	/**
	 * 删除标记
	 */
	public static final String DEL_FLAG = "del_flag";

	/**
	 * 编码
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * JSON 资源
	 */
	public static final String CONTENT_TYPE = "application/json; charset=utf-8";

	/**
	 * 阿里大鱼
	 */
	public static final String ALIYUN_SMS = "aliyun_sms";

	/**
	 * 路由信息Redis保存的key
	 */
	public static final String ROUTE_KEY = "_ROUTE_KEY";

	// ================================服务名称常量=======================================
	/**
	 * 配置服务
	 */
	public static final String SERVICE_NAME_CONFIG = "jusf-services-config-server";

	/**
	 * 注册服务
	 */
	public static final String SERVICE_NAME_REGISTRY = "jusf-services-registry-server";

	/**
	 * 网关服务
	 */
	public static final String SERVICE_NAME_ZUUL = "jusf-services-zuul-server";

	/**
	 * 监控服务
	 */
	public static final String SERVICE_NAME_MONITOR = "jusf-services-monitor-server";

	/**
	 * 认证服务
	 */
	public static final String SERVICE_NAME_AUTH = "jusf-services-auth-server";

	/**
	 * 数据采集服务
	 */
	public static final String SERVICE_NAME_DAQ = "jusf-services-daq-server";

	/**
	 * 运营服务
	 */
	public static final String SERVICE_NAME_OPS = "jusf-services-ops-server";

	/**
	 * 可视化服务
	 */
	public static final String SERVICE_NAME_VISUL = "jusf-services-visul-server";

}
