package com.info.baymax.common.crypto;

public enum CryptoType {
	AES("AES(", ")"), DES("DES(", ")"), DESEDE("DESEDE(", ")"), BASE64("BASE64(", ")");

	private final String prefix;
	private final String suffix;

	private CryptoType(String prefix, String suffix) {
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

}
