package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.content.service.ArticleCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("articleCategoryRootListDirective")
public class ArticleCategoryRootListDirective extends BaseDirective {
	private static final String ARTICLECATEGORIES = "articleCategories";

	@Autowired
	private ArticleCategoryService articleCategoryService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList;
		if (bool)
			localList = this.articleCategoryService
					.findRoots(localInteger, str);
		else
			localList = this.articleCategoryService.findRoots(localInteger);
		setFreemarker(ARTICLECATEGORIES, localList, env, body);
	}
}