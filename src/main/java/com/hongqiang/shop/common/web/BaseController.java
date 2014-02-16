/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hongqiang.shop.common.web;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.template.FlashMessageDirective;
import com.hongqiang.shop.common.utils.DateUtils;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.SpringContextHolder;
import com.hongqiang.shop.website.entity.Log;

/**
 * 控制器支持类
 * 
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	protected static final String ERROR_PAGE = "/admin/common/error";
	protected static final String SHOP_ERROR_PAGE = "/shop/common/error";

	protected static final Message ADMIN_ERROR = Message.error("admin.message.error", new Object[0]);
	protected static final Message ADMIN_SUCCESS = Message.success("admin.message.success", new Object[0]);

	protected static final Message SHOP_ERROR = Message.error("shop.message.error", new Object[0]);
	protected static final Message SHOP_SUCCESS = Message.success("shop.message.success", new Object[0]);

	private static final String CONSTRAINT_VIOLATIONS = "constraintViolations";// 验证失败的关键字

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param redirectAttributes 重定向属性
	 * @param object  验证的实体对象
	 * @param groups  验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 request中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes,Object object, Class<?>[] groups) {
		Set<?> set = this.validator.validate(object, groups);
		if (set.isEmpty())
			return true;
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS, set, 0);
		return false;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object  验证的实体对象
	 * @param groups  验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 request中
	 */
	protected boolean beanValidator(Object object, Class<?>[] groups) {
		Set<?> set = this.validator.validate(object, groups);
		if (set.isEmpty())
			return true;
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS, set, 0);
		return false;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param beanType 验证的实体类型
	 * @param propertyName 验证的属性名称
	 * @param object  验证的实体对象
	 * @param groups  验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 request中
	 */
	protected boolean beanValidator(Class<?> beanType, String propertyName, Object object, Class<?>[] groups) {
		Set<?> set = this.validator.validateValue(beanType, propertyName, object, groups);
		if (set.isEmpty())
			return true;
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS, set, 0);
		return false;
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param redirectAttributes 重定向属性
	 * @param messages 消息实体类
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,Message messages) {
		if ((redirectAttributes != null) && (messages != null))
			redirectAttributes.addFlashAttribute(FlashMessageDirective.FLASH_MESSAGE_ATTRIBUTE_NAME,messages);
	}


	/**
	 * 把价钱转换为带前后缀的字符串形式，如amount为5，则可以输入“￥5元”
	 * 
	 * @param amount 总价的金额
	 * @param hasCurrencySign 价钱的前缀，如美元的$，人民币的￥
	 * @param hasCurrencyUnit 价钱的单位，如美元的dollar，人民币的“元”
	 * @return 总价金额的字符串形式，可能包含价钱的前后缀
	 */
	protected String addMessage(BigDecimal amount,boolean hasCurrencySign, boolean hasCurrencyUnit) {
		Setting setting = SettingUtils.get();
		String currency = setting.setScale(amount).toString();
		if (hasCurrencySign)
			currency = setting.getCurrencySign() + currency;
		if (hasCurrencyUnit)
			currency = currency + setting.getCurrencyUnit();
		return currency;
	}

	/**
	 * 和stringUtils类中的getMessage方法相同， 获得i18n字符串,即支持多语言
	 * 
	 * @param code 编码类型
	 * @param args 参数
	 * @return 国家化语言
	 */
	protected String addMessage(String code, Object[] args) {
		return SpringContextHolder.getMessage(code, args);
	}

	/**
	 * 记录内容到数据库中
	 * 
	 * @param content 内容
	 */
	protected void addMessage(String content) {
		if (content != null) {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME,content, 0);
		}
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

}
