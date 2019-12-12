package com.merce.woven.dsp.data.sys.crypto.encode;

import java.io.UnsupportedEncodingException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 密码匹配器
 * @author jingwei.yang
 * @date 2019年5月14日 下午6:21:45
 */
@Component("passwordEncoder")
public class Md5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null || rawPassword.length() == 0) {
            throw new IllegalArgumentException("Bad rawPassword");
        }
        try {
            return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Password encode error!");
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return (rawPassword != null && rawPassword.length() > 0) && (encodedPassword != null && encodedPassword.length() > 0) && encode(rawPassword).equals(encodedPassword);
    }

}
