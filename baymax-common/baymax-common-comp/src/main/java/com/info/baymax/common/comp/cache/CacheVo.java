package com.info.baymax.common.comp.cache;

import java.io.Serializable;

/**
 * 说明：缓存请求包装类. <br>
 * <p>
 *
 * Copyright (c) 2016 优识云创(YSYC)
 *
 * @author Uknower-yjw
 * @date 2016-5-4 下午7:36:37
 */
public class CacheVo implements Serializable {
	private static final long serialVersionUID = -3007790670312954774L;

	private String cacheName;
	private String alias;
	private String paramKey;
	private String paramValue;

	public CacheVo() {
	}

	public CacheVo(String cacheName, String alias) {
		this.cacheName = cacheName;
		this.alias = alias;
	}

	public CacheVo(String cacheName, String paramKey, String paramValue) {
		this.cacheName = cacheName;
		this.paramKey = paramKey;
		this.paramValue = paramValue;
	}

	public CacheVo(String cacheName, String alias, String paramKey, String paramValue) {
		this.cacheName = cacheName;
		this.alias = alias;
		this.paramKey = paramKey;
		this.paramValue = paramValue;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((cacheName == null) ? 0 : cacheName.hashCode());
		result = prime * result + ((paramKey == null) ? 0 : paramKey.hashCode());
		result = prime * result + ((paramValue == null) ? 0 : paramValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheVo other = (CacheVo) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (cacheName == null) {
			if (other.cacheName != null)
				return false;
		} else if (!cacheName.equals(other.cacheName))
			return false;
		if (paramKey == null) {
			if (other.paramKey != null)
				return false;
		} else if (!paramKey.equals(other.paramKey))
			return false;
		if (paramValue == null) {
			if (other.paramValue != null)
				return false;
		} else if (!paramValue.equals(other.paramValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CacheVo [cacheName=" + cacheName + ", alias=" + alias + ", paramKey=" + paramKey + ", paramValue=" + paramValue + "]";
	}
}
