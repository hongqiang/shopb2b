package com.hongqiang.shop.modules.util.service;

public interface CacheService {
	public String getDiskStorePath();

	public int getCacheSize();

	public void clear();
}