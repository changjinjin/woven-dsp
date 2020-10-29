package com.info.baymax.security.cas.reactive.client.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginParamDto implements Serializable {
    private static final long serialVersionUID = 3264301229230828626L;

    private String username;
    private String password;
    private String time;
}
