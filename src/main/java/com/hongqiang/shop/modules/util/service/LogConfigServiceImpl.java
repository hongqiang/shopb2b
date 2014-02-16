package com.hongqiang.shop.modules.util.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.modules.utils.LogConfig;

@Service
public class LogConfigServiceImpl implements LogConfigService {
	@Cacheable({ "logConfig" })
	public List<LogConfig> getAll() {
		try {
			File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
			Document localDocument = new SAXReader().read(localFile);
			@SuppressWarnings("unchecked")
			List<LogConfig> localList = localDocument
					.selectNodes("/shophq/logConfig");
			ArrayList<LogConfig> localArrayList = new ArrayList<LogConfig>();
			Iterator<LogConfig> localIterator = localList.iterator();
			while (localIterator.hasNext()) {
				Element localElement = (Element) localIterator.next();
				String str1 = localElement.attributeValue("operation");
				String str2 = localElement.attributeValue("urlPattern");
				LogConfig localLogConfig = new LogConfig();
				localLogConfig.setOperation(str1);
				localLogConfig.setUrlPattern(str2);
				localArrayList.add(localLogConfig);
			}
			return localArrayList;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
}