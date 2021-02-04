package com.info.baymax.common.validation.config;

import com.info.baymax.common.validation.passay.pwd.PwdMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.passay.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码校验规则配置
 *
 * @author jingwei.yang
 * @date 2021年2月3日 上午11:21:45
 */
@Getter
@Setter
@ConfigurationProperties(prefix = PassayProperties.PREFIX)
public class PassayProperties {
    public static final String PREFIX = "passay";

    /**
     * 密码模式：
     */
    private PwdMode pwdMode = PwdMode.SIMPLE;

    public boolean isStrict() {
        return pwdMode != null && pwdMode.equals(PwdMode.STRICT);
    }

    /**
     * 密码校验规则，默认符合简单模式规则
     */
    private Rules rules = new Rules();

    public List<Rule> getPassayRules(PasswordData passwordData) {
        switch (pwdMode) {
            case STRICT:
                return strictRules(passwordData);
            case CUSTOM:
                return customRules(passwordData, false);
            default:
                return Arrays.asList(//
                    new WhitespaceRule(), new LengthRule(8, 30));
        }
    }

    private List<Rule> strictRules(PasswordData passwordData) {
        return customRules(passwordData, true);
    }

    private List<Rule> customRules(PasswordData passwordData, boolean isStrict) {
        List<Rule> rs = new ArrayList<Rule>();
        Whitespace whitespace = rules.getWhitespace();
        if (whitespace.isEnabled() || isStrict) {
            rs.add(new WhitespaceRule());
        }
        Length length = rules.getLength();
        if (length.isEnabled() || isStrict) {
            rs.add(new LengthRule(length.getMinLength(), length.getMaxLength()));
        }
        CharacterCharacteristics characterCharacteristics = rules.getCharacterCharacteristics();
        if (characterCharacteristics.isEnabled() || isStrict) {
            List<Character> characters = characterCharacteristics.getCharacters();
            rs.add(new CharacterCharacteristicsRule(characterCharacteristics.getNumCharacteristics(),
                characters.stream().map(t -> new CharacterRule(t.getCharacterData(), t.getNumCharacters()))
                    .collect(Collectors.toList())));
        }
        IllegalSequence illegalSequence = rules.getIllegalSequence();
        if (illegalSequence.isEnabled() || isStrict) {
            rs.add(new IllegalSequenceRule(illegalSequence.getSequenceData(), illegalSequence.getSequenceLength(),
                illegalSequence.isWrapSequence()));
        }

        // DictionarySubstring dictionarySubstring = rules.getDictionarySubstring();
        // if (dictionarySubstring.isEnabled() || isStrict) {
        // rs.add(new DictionarySubstringRule(
        // new WordListDictionary(new ArrayWordList(new String[] { passwordData.getUsername() }, false))));
        // }

        History history = rules.getHistory();
        if (history.isEnabled() || isStrict) {
            rs.add(new SourceRule());
            rs.add(new HistoryRule());
        }

        Username username = rules.getUsername();
        if (username.isEnabled() || isStrict) {
            rs.add(new UsernameRule());
        }
        return rs;
    }

    @Getter
    @Setter
    public static class Rules {

        /**
         * 1.密码是否允许有空格，如：密码不允许有空格
         */
        private Whitespace whitespace = new Whitespace();

        /**
         * 2.定义密码的长度限制，如：密码长度8~30位
         */
        private Length length = new Length();

        /**
         * 3.检查密码是否满足定义的N个规则，如：密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类
         */
        private CharacterCharacteristics characterCharacteristics = new CharacterCharacteristics();

        /**
         * 4.非法字符限制，如：特殊字符
         */
        private IllegalSequence illegalSequence = new IllegalSequence();

        // /**
        // * 5.检查密码是否包含指定字典，如：密码应避免账号信息及其大小写变换
        // */
        // private DictionarySubstring dictionarySubstring = new DictionarySubstring();

        /**
         * 6.检查密码是否包含任何历史密码引用，如：密码与上次密码相同
         */
        private History history = new History();

        /**
         * 7.检查密码是否包含用户名，如：密码包含用户名字符串
         */
        private Username username = new Username();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Enabled {
        /**
         * Is the rule enabled,default false
         */
        protected boolean enabled = false;

        public Enabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    // 1.密码不能有空格
    @Setter
    @Getter
    public static final class Whitespace extends Enabled {
        public Whitespace() {
            super(true);
        }
    }

    // 2.密码长度8~30位
    @Setter
    @Getter
    @AllArgsConstructor
    public static final class Length extends Enabled {
        public Length() {
            super(true);
        }

        /**
         * Stores the minimum length of a password.
         */
        private int minLength = 8;
        /**
         * Stores the maximum length of a password.
         */
        private int maxLength = 30;
    }

    // 3.密码应涵盖大写字母、小写字母、数字和特殊符号4类中至少3类
    @Setter
    @Getter
    @AllArgsConstructor
    public static final class CharacterCharacteristics extends Enabled {
        public CharacterCharacteristics() {
            super(false);
        }

        /**
         * Number of rules to enforce. Default value is 1.
         */
        private int numCharacteristics = 3;

        /**
         * Rules to apply when checking a password.
         */
        private List<Character> characters = Arrays.asList(new Character(EnglishCharacterData.UpperCase, 1),
            new Character(EnglishCharacterData.LowerCase, 1), new Character(EnglishCharacterData.Digit, 1),
            new Character(EnglishCharacterData.Special, 1));
    }

    // 密码应涵盖大写字符
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Character {
        /**
         * Character data for this rule.
         */
        private EnglishCharacterData characterData;

        /**
         * Number of characters to require. Default value is 1.
         */
        private int numCharacters = 1;

    }

    // 4.密码应避免键盘排序，最多可以连续两位
    @Setter
    @Getter
    @AllArgsConstructor
    public static final class IllegalSequence extends Enabled {
        public IllegalSequence() {
            super(false);
        }

        /**
         * Sequence data for this rule.
         */
        private EnglishSequenceData sequenceData = EnglishSequenceData.USQwerty;

        /**
         * Number of characters in sequence to match.
         */
        private int sequenceLength = 3;

        /**
         * Whether or not to wrap a sequence when searching for matches.
         */
        private boolean wrapSequence = false;
    }

    // 5.密码应避免账号信息及其大小写变换
    @Setter
    @Getter
    public static final class DictionarySubstring extends Enabled {
        public DictionarySubstring() {
            super(false);
        }
    }

    // 6.不能跟历史密码相同
    @Setter
    @Getter
    public static final class History extends Enabled {
        public History() {
            super(false);
        }
    }

    // 7.密码不包含用户名
    @Setter
    @Getter
    public static final class Username extends Enabled {
        public Username() {
            super(false);
        }
    }
}
