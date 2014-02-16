/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.hongqiang.shop.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.hongqiang.shop.common.utils.model.CommonAttributes;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * FreeMarkers工具类
 * 
 * @author ThinkGem
 * @version 2013-01-15
 */
public class FreeMarkers {

	private static final ConvertUtilsBean convertUtilsBean = new ShopConvertUtils();

	static {
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		convertUtilsBean.register(localDateConverter, Date.class);
	}

	public static String renderString(String templateString,
			Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString),
					new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Configuration buildConfiguration(String directory)
			throws IOException {
		Configuration cfg = new Configuration();
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		return cfg;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getParameter(String name, Class<T> type,
			Map<String, TemplateModel> params) throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(type);
		Assert.notNull(params);
		TemplateModel localTemplateModel = (TemplateModel) params.get(name);
		if (localTemplateModel == null)
			return null;
		Object localObject = DeepUnwrap.unwrap(localTemplateModel);
		return (T) convertUtilsBean.convert(localObject, type);
	}

	public static TemplateModel getVariable(String name, Environment env)
			throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		return env.getVariable(name);
	}

	public static void setVariable(String name, Object value, Environment env)
			throws TemplateModelException {
		Assert.hasText(name);
		Assert.notNull(env);
		if ((value instanceof TemplateModel))
			env.setVariable(name, (TemplateModel) value);
		else
			env.setVariable(name, ObjectWrapper.BEANS_WRAPPER.wrap(value));
	}

	public static void setVariables(Map<String, Object> variables,
			Environment env) throws TemplateModelException {
		Assert.notNull(variables);
		Assert.notNull(env);
		Iterator<Entry<String, Object>> localIterator = variables.entrySet()
				.iterator();
		while (localIterator.hasNext()) {
			Entry<String, Object> localEntry = (Entry<String, Object>) localIterator
					.next();
			String str = (String) localEntry.getKey();
			Object localObject = localEntry.getValue();
			if ((localObject instanceof TemplateModel))
				env.setVariable(str, (TemplateModel) localObject);
			else
				env.setVariable(str,
						ObjectWrapper.BEANS_WRAPPER.wrap(localObject));
		}
	}

	public static void main(String[] args) throws IOException {
		// // renderString
		// Map<String, String> model =
		// com.google.common.collect.Maps.newHashMap();
		// model.put("userName", "calvin");
		// String result = FreeMarkers.renderString("hello ${userName}", model);
		// System.out.println(result);
		// // renderTemplate
		// Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
		// Template template = cfg.getTemplate("testTemplate.ftl");
		// String result2 = FreeMarkers.renderTemplate(template, model);
		// System.out.println(result2);

		// Map<String, String> model =
		// com.google.common.collect.Maps.newHashMap();
		// model.put("userName", "calvin");
		// String result =
		// FreeMarkers.renderString("hello ${userName} ${r'${userName}'}",
		// model);
		// System.out.println(result);
	}

}
