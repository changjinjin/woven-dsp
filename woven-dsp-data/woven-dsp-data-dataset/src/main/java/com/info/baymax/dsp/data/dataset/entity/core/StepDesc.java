package com.info.baymax.dsp.data.dataset.entity.core;

import com.google.common.collect.Lists;
import com.info.baymax.common.crypto.CryptoBean;
import com.info.baymax.common.crypto.CryptoType;
import com.info.baymax.common.crypto.delegater.CryptorDelegater;
import com.info.baymax.dsp.data.dataset.entity.ConfigObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class StepDesc implements Cloneable, Serializable, CryptoBean {
    private static final long serialVersionUID = -598721136723181311L;

    @ApiModelProperty("流程ID")
    private String flowId;

    @ApiModelProperty("X轴坐标")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("X轴坐标")
    private int x = 0;

    @ApiModelProperty("Y轴坐标")
    private int y = 0;

    @ApiModelProperty("额外配置信息")
    private ConfigObject otherConfigurations;

    @ApiModelProperty("输入配置信息")
    private List<ConfigObject> inputConfigurations;

    @ApiModelProperty("输出配置信息")
    private List<ConfigObject> outputConfigurations;

    @ApiModelProperty("库信息")
    private List<String> libs;

    @ApiModelProperty("继承信息")
    private String implementation;

    public StepDesc() {
        this("", "", "", null, null, null, 0, 0);
    }

    public StepDesc(String id, String name, String type, ConfigObject otherConfigurations,
                    List<ConfigObject> inputConfigurations, List<ConfigObject> outputConfigurations, int x, int y) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.otherConfigurations = otherConfigurations;
        this.inputConfigurations = inputConfigurations;
        this.outputConfigurations = outputConfigurations;
        this.x = x;
        this.y = y;
    }

    public StepDesc(String id, String name, StepDesc stepDef, ConfigObject otherConfigurations,
                    List<ConfigObject> inputConfigurations, List<ConfigObject> outputConfigurations) {
        this.id = id;
        this.name = name;
        this.type = stepDef.getType();
        this.implementation = stepDef.getImplementation();
        this.libs = stepDef.getLibs();
        this.otherConfigurations = otherConfigurations;
        this.inputConfigurations = inputConfigurations;
        this.outputConfigurations = outputConfigurations;
    }

    @Override
    public void encrypt(CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        if (otherConfigurations != null && !otherConfigurations.isEmpty()) {
            switch (cryptoType) {
                case BASE64:
                    break;
                case AES:
                    Object password = otherConfigurations.get("password");
                    if (password != null) {
                        otherConfigurations.replace("password",
                            ciphertext(password.toString(), cryptoType, cryptorDelegater));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void decrypt(CryptorDelegater cryptorDelegater) {
        if (otherConfigurations != null && !otherConfigurations.isEmpty()) {
            switch (type) {
                case "sql":
                case "sqlsource":
                    Object sql = otherConfigurations.get("sql");
                    if (sql != null) {
                        otherConfigurations.replace("sql", plaintext(sql.toString(), cryptorDelegater));
                    }
                    break;
                case "filter":
                case "split":
                    Object condition = otherConfigurations.get("condition");
                    if (condition != null) {
                        otherConfigurations.replace("condition", plaintext(condition.toString(), cryptorDelegater));
                    }
                    break;
                case "transform":
                    Object expressionsObj = otherConfigurations.get("expressions");
                    if (expressionsObj != null) {
                        Class<? extends Object> clazz = expressionsObj.getClass();
                        List<String> newExpressions = Lists.newArrayList();
                        if (Iterable.class.isAssignableFrom(clazz)) {
                            Iterator<?> iterator = ((Iterable<?>) expressionsObj).iterator();
                            iterator.forEachRemaining(exp -> {
                                newExpressions.add(plaintext(exp.toString(), cryptorDelegater));
                            });
                        } else if (clazz.isArray()) {
                            for (int i = 0; i < Array.getLength(expressionsObj); i++) {
                                newExpressions.add(plaintext(Array.get(expressionsObj, i).toString(), cryptorDelegater));
                            }
                        }
                        otherConfigurations.replace("expressions", newExpressions);
                    }
                    break;
                case "validate":
                    Object validationRulesObj = otherConfigurations.get("validationRules");
                    if (validationRulesObj != null) {
                        Class<? extends Object> clazz = validationRulesObj.getClass();
                        List<Map<String, Object>> newValidationRules = Lists.newArrayList();
                        if (Iterable.class.isAssignableFrom(clazz)) {
                            Iterator<?> iterator = ((Iterable<?>) validationRulesObj).iterator();
                            iterator.forEachRemaining(exp -> {
                                Map<String, Object> map = (Map<String, Object>) exp;
                                map.replace("expression",
                                    plaintext(map.getOrDefault("expression", "").toString(), cryptorDelegater));
                                newValidationRules.add(map);
                            });
                        } else if (clazz.isArray()) {
                            for (int i = 0; i < Array.getLength(validationRulesObj); i++) {
                                Map<String, Object> map = (Map<String, Object>) Array.get(validationRulesObj, i);
                                map.replace("expression",
                                    plaintext(map.getOrDefault("expression", "").toString(), cryptorDelegater));
                                newValidationRules.add(map);
                            }
                        }
                        otherConfigurations.replace("validationRules", newValidationRules);
                    }
                    break;
                default:
                    break;
            }

            // 密码都需要解密
            Object password = otherConfigurations.get("password");
            if (password != null) {
                otherConfigurations.replace("password", plaintext(password.toString(), cryptorDelegater));
            }
        }
    }
}
