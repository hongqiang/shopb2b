/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hongqiang.shop.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * 
 * @author ThinkGem
 * @version 2013-01-15
 */
public class CookieUtils {

	/**
	 * 设置 Cookie（生成时间为1天）
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value) {
		setCookie(response, name, value, 60 * 60 * 24);
	}

	/**
	 * 设置 Cookie
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 * @param maxAge
	 *            生存时间（单位秒）
	 * @param uri
	 *            路径
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		try {
			cookie.setValue(URLEncoder.encode(value, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge, String path, String domain, Boolean secure) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie localCookie = new Cookie(name, value);
			if (maxAge != null)
				localCookie.setMaxAge(maxAge.intValue());
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			if (secure != null)
				localCookie.setSecure(secure.booleanValue());
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
			localUnsupportedEncodingException1.printStackTrace();
		}
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge) {
		Setting localSetting = SettingUtils.get();
		setCookie(request, response, name, value, maxAge,
				localSetting.getCookiePath(), localSetting.getCookieDomain(),
				null);
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		Setting localSetting = SettingUtils.get();
		setCookie(request, response, name, value, null,
				localSetting.getCookiePath(), localSetting.getCookieDomain(),
				null);
	}

	/**
	 * 获得指定Cookie的值
	 * 
	 * @param name
	 *            名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, null, name, false);
	}

	/**
	 * 获得指定Cookie的值，并删除。
	 * 
	 * @param name
	 *            名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		return getCookie(request, response, name, true);
	}

	/**
	 * 获得指定Cookie的值
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param name
	 *            名字
	 * @param isRemove
	 *            是否移除
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request,
			HttpServletResponse response, String name, boolean isRemove) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (isRemove) {
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String path,
			String domain) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie localCookie = new Cookie(name, null);
			localCookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
			localUnsupportedEncodingException1.printStackTrace();
		}
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		Setting localSetting = SettingUtils.get();
		removeCookie(request, response, name, localSetting.getCookiePath(),
				localSetting.getCookieDomain());
	}
}
