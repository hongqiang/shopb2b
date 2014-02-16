package com.hongqiang.shop.common.template;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.common.utils.Order;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public abstract class BaseDirective implements TemplateDirectiveModel {
	private static final String USE_CACHE = "useCache";
	private static final String CACHE_REGION = "cacheRegion";
	private static final String ID = "id";
	//由页面设定多少个，然后调用findpage或者findlist得到个数为count的结果集，返回给前端
	private static final String COUNT = "count";
	private static final String ORDER_BY = "orderBy";
	private static final String ASTERISK = "\\s*,\\s*";
	private static final String ADDITION = "\\s+";

	protected boolean setFreemarker(Environment paramEnvironment,
			Map<String, TemplateModel> paramMap) throws TemplateModelException {
		Boolean localBoolean = FreeMarkers.getParameter(USE_CACHE,
				Boolean.class, paramMap);
		return localBoolean != null ? localBoolean.booleanValue() : true;
	}

	protected String getFreemarkerCacheRegion(Environment paramEnvironment,
			Map<String, TemplateModel> paramMap) throws TemplateModelException {
		String str = FreeMarkers.getParameter(CACHE_REGION,
				String.class, paramMap);
		return str != null ? str : paramEnvironment.getTemplate().getName();
	}

	protected Long getFreemarkerLong(Map<String, TemplateModel> paramMap)
			throws TemplateModelException {
		return FreeMarkers.getParameter(ID, Long.class, paramMap);
	}

	protected Integer getFreemarkerCount(Map<String, TemplateModel> paramMap)
			throws TemplateModelException {
		return FreeMarkers.getParameter(COUNT, Integer.class,
				paramMap);
	}

	protected List<Filter> getFreemarkerFilter(
			Map<String, TemplateModel> paramMap, Class<?> paramClass,
			String[] paramArrayOfString) throws TemplateModelException {
		ArrayList<Filter> localArrayList = new ArrayList<Filter>();
		PropertyDescriptor[] arrayOfPropertyDescriptor1 = PropertyUtils
				.getPropertyDescriptors(paramClass);
		for (PropertyDescriptor localPropertyDescriptor : arrayOfPropertyDescriptor1) {
			String str = localPropertyDescriptor.getName();
			Class<?> localClass = localPropertyDescriptor.getPropertyType();
			if ((ArrayUtils.contains(paramArrayOfString, str))
					|| (!paramMap.containsKey(str)))
				continue;
			Object localObject = FreeMarkers.getParameter(str, localClass,
					paramMap);
			localArrayList.add(Filter.eq(str, localObject));
		}
		return localArrayList;
	}

	protected List<Order> getFreemarkerOrder(
			Map<String, TemplateModel> paramMap, String[] paramArrayOfString) throws TemplateModelException {
		String str1 = StringUtils.trim(FreeMarkers.getParameter(
				ORDER_BY, String.class, paramMap));
		ArrayList<Order> localArrayList = new ArrayList<Order>();
		if (StringUtils.isNotEmpty(str1)) {
			String[] arrayOfString1 = str1.split(ASTERISK);
			for (String str2 : arrayOfString1) {
				if (!StringUtils.isNotEmpty(str2))
					continue;
				Object localObject = null;
				Order.Direction localDirection = null;
				String[] arrayOfString3 = str2.split(ADDITION);
				if (arrayOfString3.length == 1) {
					localObject = arrayOfString3[0];
				} else {
					if (arrayOfString3.length < 2)
						continue;
					localObject = arrayOfString3[0];
					try {
						localDirection = Order.Direction
								.valueOf(arrayOfString3[1]);
					} catch (IllegalArgumentException localIllegalArgumentException) {
						continue;
					}
				}
				if (ArrayUtils.contains(paramArrayOfString, localObject))
					continue;
				localArrayList.add(new Order((String)localObject, localDirection));
			}
		}
		return localArrayList;
	}

	protected void setFreemarker(String paramString, Object paramObject,
			Environment paramEnvironment,
			TemplateDirectiveBody paramTemplateDirectiveBody)
			throws TemplateException, IOException {
		TemplateModel localTemplateModel = FreeMarkers.getVariable(paramString,
				paramEnvironment);
		FreeMarkers.setVariable(paramString, paramObject, paramEnvironment);
		paramTemplateDirectiveBody.render(paramEnvironment.getOut());
		FreeMarkers.setVariable(paramString, localTemplateModel,
				paramEnvironment);
	}

	protected void setFreemarker(Map<String, Object> paramMap,
			Environment paramEnvironment,
			TemplateDirectiveBody paramTemplateDirectiveBody)
			throws TemplateException, IOException {
		HashMap<String, Object> localHashMap = new HashMap<String, Object>();
		Iterator<String> localIterator = paramMap.keySet().iterator();
		while (localIterator.hasNext()) {
			String str = localIterator.next();
			TemplateModel localTemplateModel = FreeMarkers.getVariable(str,
					paramEnvironment);
			localHashMap.put(str, localTemplateModel);
		}
		FreeMarkers.setVariables(paramMap, paramEnvironment);
		paramTemplateDirectiveBody.render(paramEnvironment.getOut());
		FreeMarkers.setVariables(localHashMap, paramEnvironment);
	}
}