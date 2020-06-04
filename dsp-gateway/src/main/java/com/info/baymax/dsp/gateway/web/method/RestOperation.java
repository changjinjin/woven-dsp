package com.info.baymax.dsp.gateway.web.method;

import com.info.baymax.common.utils.HashUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString(doNotUseGetters = true)
@ApiModel
public class RestOperation implements Serializable {
	private static final long serialVersionUID = -7474978343387617372L;

	@ApiModelProperty("主键")
	private String id;

	@ApiModelProperty("服务名称")
	private String serviceName;

	@ApiModelProperty("接口所属分组")
	private String groupName;

	@ApiModelProperty("接口所属标签")
	private String[] tags;

	@ApiModelProperty("接口请求方法")
	private String method;

	@ApiModelProperty("接口基本路径")
	private String basePath;

	@ApiModelProperty("接口相对路径")
	private String relativePath;

	@ApiModelProperty("接口全路径")
	private String fullPath;

	@ApiModelProperty("接口概要")
	private String summary;

	@ApiModelProperty("接口描述")
	private String description;

	@ApiModelProperty("接口操作ID")
	private String operationId;

	@ApiModelProperty("接口消费信息")
	private String[] consumes;

	@ApiModelProperty("接口生产信息")
	private String[] produces;

	@ApiModelProperty("接口是否弃用：true-是，false-否")
	private Boolean deprecated;

	@ApiModelProperty("是否启用权限控制：true-是，false-否，默认false")
	private Boolean enabled;

	public RestOperation(String method, String basePath, String relativePath) {
		this.method = method;
		this.basePath = basePath;
		this.relativePath = relativePath;
		this.summary = relativePath;
		this.description = relativePath;
	}

	public RestOperation(String serviceName, String groupName, String[] tags, String method, String basePath,
			String relativePath, String summary, String description, String operationId, String[] consumes,
			String[] produces, Boolean deprecated) {
		this.serviceName = serviceName;
		this.groupName = groupName;
		this.tags = tags;
		this.method = method;
		this.basePath = basePath;
		this.relativePath = relativePath;
		this.summary = summary;
		this.description = description;
		this.operationId = operationId;
		this.consumes = consumes;
		this.produces = produces;
		this.deprecated = deprecated;
	}

	public static RestOperation unkown(String method, String requestPath) {
		return new RestOperation(method, "/", requestPath);
	}

	public String getDescription() {
		if (StringUtils.isEmpty(description)) {
			this.description = this.summary;
		}
		return description;
	}

	public String getFullPath() {
		if (StringUtils.isEmpty(fullPath) && basePath != null && relativePath != null) {
			this.fullPath = trimSlash(getBasePath() + getRelativePath());
		}
		return fullPath;
	}

	public String getId() {
		if (StringUtils.isEmpty(id) && StringUtils.isNotEmpty(getFullPath())) {
			this.id = HashUtil.hashKey(operationKey());
		}
		return id;
	}

	private String trimSlash(String src) {
		return src.replaceAll("//", "/");
	}

	public String operation() {
		if (StringUtils.isNotEmpty(summary)) {
			return summary;
		} else if (StringUtils.isNotEmpty(description)) {
			return description;
		} else {
			return getFullPath();
		}
	}

	public String operationKey() {
		if (relativePath != null && method != null) {
			return getFullPath() + "@" + method.toUpperCase();
		}
		return null;
	}

}
