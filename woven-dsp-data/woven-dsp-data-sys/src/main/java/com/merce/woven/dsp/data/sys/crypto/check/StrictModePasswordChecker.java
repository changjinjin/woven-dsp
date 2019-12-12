package com.merce.woven.dsp.data.sys.crypto.check;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.DictionarySubstringRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;

/**
 * 严格模式密码检查器
 *
 * @author jingwei.yang
 * @date 2019年9月23日 下午12:05:19
 */
public class StrictModePasswordChecker implements PasswordChecker {

	/**
	 * <pre>
	 * 需要强制的rule有:
	 * 	1.密码长度应至少8位；
	 *  2.密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类；
	 *  3.密码应避免账号信息及其大小写变换；
	 *  4.密码应避免键盘排序，最多可以连续两位
	 * </pre>
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Override
	public boolean check(PasswordData passwordData) throws PasswordCheckException {
		// 1.密码不能有空格
		WhitespaceRule r1 = new WhitespaceRule();
		// 2.密码长度8~30位
		LengthRule r2 = new LengthRule(8, 30);
		// 3.密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类
		CharacterCharacteristicsRule r3 = new CharacterCharacteristicsRule(3,
				Arrays.asList(new CharacterRule(EnglishCharacterData.UpperCase, 1),
						new CharacterRule(EnglishCharacterData.LowerCase, 1),
						new CharacterRule(EnglishCharacterData.Digit, 1),
						new CharacterRule(EnglishCharacterData.Special, 1)));
		// 4.密码应避免键盘排序，最多可以连续两位
		IllegalSequenceRule r4 = new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, false);
		// 5.密码应避免账号信息及其大小写变换
		DictionarySubstringRule r5 = new DictionarySubstringRule(
				new WordListDictionary(new ArrayWordList(new String[] { passwordData.getUsername() }, false)));

		PasswordValidator validator = new PasswordValidator(new PropertiesMessageResolver(getDefaultProperties()), r1,
				r2, r3, r4, r5);

		RuleResult result = validator.validate(passwordData);
		if (!result.isValid()) {
			for (String msg : validator.getMessages(result)) {
				throw new PasswordCheckException(msg);
			}
		}
		return result.isValid();
	}

	public static final String LOCALE_MESSAGE_PATH = "/i18n/messages_zh_CN.properties";
	public static final String DEFAULT_MESSAGE_PATH = "/messages.properties";

	public static Properties getDefaultProperties() {
		final Properties props = new Properties();
		InputStream in = null;
		try {
			in = PropertiesMessageResolver.class.getResourceAsStream(LOCALE_MESSAGE_PATH);
			props.load(in);
		} catch (Exception e) {
			try {
				in = PropertiesMessageResolver.class.getResourceAsStream(DEFAULT_MESSAGE_PATH);
				props.load(in);
			} catch (IOException e1) {
				throw new IllegalStateException("Error loading default message properties.", e);
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
}
