package com.info.baymax.common.utils;

import java.util.regex.Pattern;

/**
 * 说明：常用正则验证. <br>
 *
 * @author jingwei.yang
 * @date 2017年9月16日 上午11:36:43
 */
public class RegexUtils {
	public static final String REGEX_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";// 手机号码
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";// 邮箱
	public static final String REGEX_INTERNETURL = "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";// 或[a-zA-z]+://[^\s]*
	public static final String REGEX_USERNAME = "^[\u4e00-\u9fa5A-Za-z0-9_]{6,20}$";// 用户名为6到20个字符，可使用汉字、英文字母、数字及下划线！
	public static final String REGEX_PASSWORD = "^[0-9A-Za-z_]{6,}$";// 密码至少6个字符，可使用字母、数字及下划线！
	public static final String REGEX_IDNUMBER = "^(\\d{6})(18|19|20)?(\\d{2})([01]\\d)([0123]\\d)(\\d{3})(\\d|X)?$";// 身份证验证，比较严格的验证
	public static final String REGEX_ATTACHMENT = "(?i).+?\\.(jpeg|JPEG|jpg|JPG|gif|GIF|bmp|BMP|png|PNG|doc|DOC|docx|DOCX|xls|XLS|xlsx|XLSX|ppt|PPT|pptx|PPTX|txt|TXT|pdf|PDF|rar|RAR|zip|ZIP|mp4|MP4|flv|FLV|mp3|MP3|wav|WAV)";
	public static final String REGEX_FLOATING_NUMBER = "^[-+]?[0-9]*\\.?[0-9]+$";
	public static final String REGEX_DATE = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
	public static final String REGEX_DATETIME = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+((20|21|22|23|[0-1])?\\d):[0-5]?\\d:[0-5]?\\d$";
	public static final String REGEX_DATE1 = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
	public static final String REGEX_DATETIME1 = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+((20|21|22|23|[0-1])?\\d):[0-5]?\\d:[0-5]?\\d$";

	// html标签正则
	public static final String REGEX_HTML_EMBED = "<embed.*?>";// flash元素
	public static final String REGEX_HTML_IMG = "<img.*?>";// 图片
	public static final String REGEX_HTML_A = "<a.*?>";// 链接

	/**
	 * 说明： 正则验证. <br>
	 *
	 * @author jingwei.yang
	 * @date 2017年9月16日 上午11:37:19
	 * @param regix
	 *            正则表达式
	 * @param s
	 *            验证字符串
	 * @return 匹配返回true，否则返回false
	 */
	public static boolean check(String regex, String s) {
		return Pattern.compile(regex).matcher(s).matches();
	}

	/**
	 * 判定邮箱正确性
	 *
	 * @param email
	 *            邮箱字段
	 * @return
	 */
	public static boolean checkEmail(String email) {
		return check(REGEX_EMAIL, email);
	}

	/**
	 * 判定手机号码正确性
	 *
	 * @param phone
	 *            用户名字段
	 * @return
	 */
	public static boolean checkPhone(String phone) {
		return check(REGEX_PHONE, phone);
	}

	/**
	 * 判定用户名正确性
	 *
	 * @param username
	 *            用户名字段
	 * @return
	 */
	public static boolean checkUsername(String username) {
		return check(REGEX_USERNAME, username);
	}

	/**
	 * 判定用户密码正确性
	 *
	 * @param password
	 *            用户密码字段
	 * @return
	 */
	public static boolean checkPassword(String password) {
		return check(REGEX_PASSWORD, password);
	}

	/**
	 * 身份证号码验证
	 *
	 * @param IDNumber
	 *            身份证号码
	 * @return
	 */
	public static boolean checkIDNumber(String IDNumber) {
		return check(REGEX_IDNUMBER, IDNumber);
	}

	/**
	 * 网络路径验证
	 *
	 * @param url
	 *            网络路径
	 * @return
	 */
	public static boolean checkInternetUrl(String internetUrl) {
		return check(REGEX_INTERNETURL, internetUrl);
	}

	/**
	 * 网络路径验证
	 *
	 * @param url
	 *            网络路径
	 * @return
	 */
	public static boolean checkAttachment(String filename) {
		return check(REGEX_ATTACHMENT, filename);
	}

	/**
	 * 网络路径验证
	 *
	 * @param url
	 *            网络路径
	 * @return
	 */
	public static boolean checkFloatingNumber(String text) {
		return check(REGEX_FLOATING_NUMBER, text);
	}

	/**
	 * 说明： 校验日期格式：YYYY-MM-DD. <br>
	 * 
	 * @author jingwei.yang
	 * @date 2017年11月17日 下午7:11:28
	 * @param text
	 * @return
	 */
	public static boolean checkDate(String text) {
		return check(REGEX_DATE, text);
	}

	/**
	 * 说明： 校验日期时间格式:YYYY-MM-DD HH:mm:ss. <br>
	 * 
	 * @author jingwei.yang
	 * @date 2017年11月17日 下午7:11:10
	 * @param text
	 * @return
	 */
	public static boolean checkDateTime(String text) {
		return check(REGEX_DATETIME, text);
	}

	// public static void main(String[] args) {
	// System.out.println(checkDate("2017-09-09"));
	// System.out.println(checkDateTime("2017-09-09 2:00:23"));
	// }
}