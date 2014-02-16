/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hongqiang.shop.common.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * Cache工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CacheUtils {

	private static CacheManager cacheManager = ((EhCacheManager) SpringContextHolder
			.getBean("cacheManager")).getCacheManager();

	public static String getDiskStorePath() {
		return cacheManager.getConfiguration().getDiskStoreConfiguration()
				.getPath();
	}

	public static int getCacheSize() {
		int i = 0;
		String[] arrayOfString1 = cacheManager.getCacheNames();
		if (arrayOfString1 != null){
			for (String str : arrayOfString1) {
				Ehcache localEhcache = cacheManager.getEhcache(str);
				if (localEhcache == null)
					continue;
				i += localEhcache.getSize();
			}
		}
		return i;
	}

	private static final String SYS_CACHE = "sysCache";

	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}

	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();
	}

	public static Object getKeys(String cacheName) {
		Object keys = getCache(cacheName).getKeys();
		if (keys != null) {
			return keys;
		}
		return null;
	}

	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}

	public static void removeAll(String cacheName) {
		getCache(cacheName).removeAll();
	}

	/**
	 * 获得一个Cache，没有则创建一个。
	 * 
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

}
