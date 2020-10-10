package com.merce.woven.cas.client.reactive.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {
    private static final long serialVersionUID = 8011997532585267354L;

    private String indexUrl;
    private String tgtUrl;
    private String tgTicket;
    private int opCode;
    private String opDesc;

    private String service;
}
