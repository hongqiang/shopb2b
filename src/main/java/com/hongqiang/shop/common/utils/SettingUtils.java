package com.hongqiang.shop.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.io.IOUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.ClassPathResource;

import com.hongqiang.shop.common.utils.model.CommonAttributes;

public final class SettingUtils {
	private static final BeanUtilsBean beanUtilsBean;

	static {
		ShopConvertUtils local1 = new ShopConvertUtils();
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(CommonAttributes.DATE_PATTERNS);
		local1.register(localDateConverter, Date.class);
		beanUtilsBean = new BeanUtilsBean(local1);
	}

	public static Setting get() {

		Setting localSetting = (Setting) CacheUtils.get(Setting.CACHE_NAME,
				Setting.CACHE_KEY);
		if (localSetting == null) {
			localSetting = new Setting();
			try {
				File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
				Document localDocument = new SAXReader().read(localFile);
				@SuppressWarnings("unchecked")
				List<org.dom4j.Element> localList = localDocument
						.selectNodes("/shophq/setting");
				Iterator<org.dom4j.Element> localIterator = localList
						.iterator();
				while (localIterator.hasNext()) {
					org.dom4j.Element localElement1 = (org.dom4j.Element) localIterator
							.next();
					String str1 = localElement1.attributeValue("name");
					String str2 = localElement1.attributeValue("value");
					try {
//						 System.out.println("key= "+str1+" , value= "+str2);
						beanUtilsBean.setProperty(localSetting, str1, str2);
					} catch (IllegalAccessException localIllegalAccessException) {
						localIllegalAccessException.printStackTrace();
					} catch (InvocationTargetException localInvocationTargetException) {
						localInvocationTargetException.printStackTrace();
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			}
			CacheUtils.put(Setting.CACHE_NAME, Setting.CACHE_KEY, localSetting);
		}
		return localSetting;
	}

	public static void set(Setting setting) {
		try {
			File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
			Document localDocument = new SAXReader().read(localFile);
			@SuppressWarnings("unchecked")
			List<org.dom4j.Element> localList = localDocument
					.selectNodes("/shophq/setting");
			Iterator<org.dom4j.Element> elementIterator = localList.iterator();
			while (elementIterator.hasNext()) {
				org.dom4j.Element element = (org.dom4j.Element) elementIterator
						.next();
				try {
					String str1 = element.attributeValue("name");
					String str2 = beanUtilsBean.getProperty(setting, str1);
					Attribute localAttribute = element.attribute("value");
					localAttribute.setValue(str2);
				} catch (IllegalAccessException localIllegalAccessException1) {
					localIllegalAccessException1.printStackTrace();
				} catch (InvocationTargetException localInvocationTargetException1) {
					localInvocationTargetException1.printStackTrace();
				} catch (NoSuchMethodException localNoSuchMethodException1) {
					localNoSuchMethodException1.printStackTrace();
				}
			}
			OutputStream outputStream = null;
			XMLWriter xmlWriter = null;
			try {
				OutputFormat localOutputFormat = OutputFormat
						.createPrettyPrint();
				localOutputFormat.setEncoding("UTF-8");
				localOutputFormat.setIndent(true);
				localOutputFormat.setIndent("\t");
				localOutputFormat.setNewlines(true);
				outputStream = new FileOutputStream(localFile);
				xmlWriter = new XMLWriter(outputStream, localOutputFormat);
				xmlWriter.write(localDocument);
			} catch (Exception localException3) {
				localException3.printStackTrace();
			} finally {
				if (xmlWriter != null)
					try {
						xmlWriter.close();
					} catch (IOException localIOException4) {
					}
				IOUtils.closeQuietly(outputStream);
			}
			CacheUtils.put(Setting.CACHE_NAME, Setting.CACHE_KEY, setting);
		} catch (Exception localException2) {
			localException2.printStackTrace();
		}
	}
}
