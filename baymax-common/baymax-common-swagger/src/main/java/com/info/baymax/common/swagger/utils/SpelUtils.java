package com.info.baymax.common.swagger.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * spel解析工具
 *
 * @author jingwei.yang
 * @date 2019年12月6日 下午5:01:23
 */
public class SpelUtils {

	/**
	 * 说明：解析方法注解上面的spel表达式. <br>
	 *
	 * @param key    表达式
	 * @param method 方法
	 * @param args   参数列表
	 * @return 解析后的结果
	 * @author jingwei.yang
	 * @date 2018年7月31日 上午9:00:11
	 */
	public static String parse(String key, Method method, Object[] args) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 使用SPEL进行key的解析
		ExpressionParser parser = new SpelExpressionParser();
		// SPEL上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		return parser.parseExpression(key).getValue(context, String.class);
	}

	public static String parse(String key, Map<String, Object> values) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		values.forEach((k, v) -> {
			context.setVariable(k, v);
		});
		return new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
	}

	public static String parse(String key, Object object) {
		return new SpelExpressionParser().parseExpression(key).getValue(new StandardEvaluationContext(object),
				String.class);
	}
}
