package com.info.baymax.security.oauth.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * 简单的授权包装类继承于<code>GrantedAuthority</code>，区别于<code>SimpleGrantedAuthority</code>，这里需要有空参的构造器，避免一些序列化框架在序列化或者反序列化时因为没有空参构造器而报错.
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:01:28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = -6829971453246039500L;

    /**
     * 权限值
     */
    private String authority;
}
