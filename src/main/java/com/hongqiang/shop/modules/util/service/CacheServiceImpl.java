package com.hongqiang.shop.modules.util.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.common.utils.CacheUtils;
import com.hongqiang.shop.common.utils.SettingUtils;

import freemarker.template.TemplateModelException;

@Service
public class CacheServiceImpl implements CacheService {
	@Autowired
	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	public String getDiskStorePath() {
		return CacheUtils.getDiskStorePath();
	}

	public int getCacheSize() {
		return CacheUtils.getCacheSize();
	}

	@CacheEvict(value = { "setting", "authorization", "logConfig", "template",
			"shipping", "area", "seo", "adPosition", "memberAttribute",
			"navigation", "tag", "friendLink", "brand", "article",
			"articleCategory", "product", "productCategory", "review",
			"consultation", "promotion" }, allEntries = true)
	public void clear() {
		this.reloadableResourceBundleMessageSource.clearCache();
		try {
			this.freeMarkerConfigurer.getConfiguration().setSharedVariable(
					"setting", SettingUtils.get());
		} catch (TemplateModelException localTemplateModelException) {
			localTemplateModelException.printStackTrace();
		}
		this.freeMarkerConfigurer.getConfiguration().clearTemplateCache();
	}
}