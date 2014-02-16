package com.hongqiang.shop.modules.utils;

import java.io.Serializable;

public class LogConfig implements Serializable {
	private static final long serialVersionUID = -1108848647938408402L;
	private String operation;
	private String urlPattern;

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getUrlPattern() {
		return this.urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
}