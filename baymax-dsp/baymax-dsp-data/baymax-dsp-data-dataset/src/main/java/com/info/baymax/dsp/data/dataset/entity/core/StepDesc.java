package com.info.baymax.dsp.data.dataset.entity.core;

import com.google.common.collect.Lists;
import com.info.baymax.common.core.crypto.CryptoBean;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.merce.woven.common.ConfigObject;
import com.merce.woven.common.StepFieldGroup;
import com.merce.woven.step.StepMiniDesc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * step完整的描述信息，包含前端参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class StepDesc extends StepMiniDesc implements CryptoBean {
	private static final long serialVersionUID = 9136912998458831599L;

	@ApiModelProperty("流程ID")
    private String flowId;

    @ApiModelProperty("X轴坐标")
    private int x = 0;

    @ApiModelProperty("Y轴坐标")
    private int y = 0;

    @ApiModelProperty("继承信息")
    private String implementation;

    @ApiModelProperty("前端配置信息")
    private ConfigObject uiConfigurations;

    public StepDesc() {
        this("", "", "", null, null, null, 0, 0);
    }

    public StepDesc(String id, String name, String type, ConfigObject otherConfigurations,
                    StepFieldGroup inputConfigurations, StepFieldGroup outputConfigurations, int x, int y) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setOtherConfigurations(otherConfigurations);
        this.setInputConfigurations(inputConfigurations);
        this.setOutputConfigurations(outputConfigurations);
        this.x = x;
        this.y = y;
    }

    public StepDesc(String id, String name, StepDesc stepDef, ConfigObject otherConfigurations,
                    StepFieldGroup inputConfigurations, StepFieldGroup outputConfigurations) {
        this.setId(id);
        this.setName(name);
        this.setType(stepDef.getType());
        this.setLibs(stepDef.getLibs());
        this.setOtherConfigurations(otherConfigurations);
        this.setInputConfigurations(inputConfigurations);
        this.setOutputConfigurations(outputConfigurations);
        this.implementation = stepDef.getImplementation();
    }

	@Override
	public void encrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        ConfigObject otherConfigurations = this.getOtherConfigurations();
        if (otherConfigurations != null && !otherConfigurations.isEmpty()) {
            switch (cryptoType) {
                case BASE64:
                    break;
                case AES:
                    Object password = otherConfigurations.get("password");
                    if (password != null) {
                        otherConfigurations.replace("password",
                            ciphertext(password.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                    }
                    break;
                default:
                    break;
            }
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public void decrypt(String secretKey, boolean wrapped, CryptoType cryptoType, CryptorDelegater cryptorDelegater) {
        ConfigObject otherConfigurations = this.getOtherConfigurations();
        String type = this.getType();
        if (otherConfigurations != null && !otherConfigurations.isEmpty()) {
            switch (type) {
                case "sql":
                case "sqlsource":
                    Object sql = otherConfigurations.get("sql");
                    if (sql != null) {
                        otherConfigurations.replace("sql", plaintext(sql.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                    }
                    break;
                case "filter":
                case "split":
                    Object condition = otherConfigurations.get("condition");
                    if (condition != null) {
                        otherConfigurations.replace("condition", plaintext(condition.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                    }
                    break;
                case "transform":
                    Object expressionsObj = otherConfigurations.get("expressions");
                    if (expressionsObj != null) {
                        Class<? extends Object> clazz = expressionsObj.getClass();
                        List<Map<String, Object>> newExpressions = Lists.newArrayList();
                        if (Iterable.class.isAssignableFrom(clazz)) {
                            Iterator<?> iterator = ((Iterable<?>) expressionsObj).iterator();
                            iterator.forEachRemaining(exp -> {
                                Map<String, Object> map = (Map<String, Object>) exp;
                                map.replace("value",
                                        plaintext(map.getOrDefault("value", "").toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                                newExpressions.add(map);

                            });
                        } else if (clazz.isArray()) {
                            for (int i = 0; i < Array.getLength(expressionsObj); i++) {
                                Map<String, Object> map = (Map<String, Object>)Array.get(expressionsObj, i);
                                map.replace("value",
                                        plaintext(map.getOrDefault("value", "").toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                                newExpressions.add(map);
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
                                    plaintext(map.getOrDefault("expression", "").toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
                                newValidationRules.add(map);
                            });
                        } else if (clazz.isArray()) {
                            for (int i = 0; i < Array.getLength(validationRulesObj); i++) {
                                Map<String, Object> map = (Map<String, Object>) Array.get(validationRulesObj, i);
                                map.replace("expression",
                                    plaintext(map.getOrDefault("expression", "").toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
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
                otherConfigurations.replace("password", plaintext(password.toString(), secretKey, wrapped, cryptoType, cryptorDelegater));
            }
        }
    }
 
    
    
}
