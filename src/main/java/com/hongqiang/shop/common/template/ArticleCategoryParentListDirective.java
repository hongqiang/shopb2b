package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.entity.ArticleCategory;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("articleCategoryParentListDirective")
public class ArticleCategoryParentListDirective extends BaseDirective {
	private static final String ARTICLECATEGORY_ID = "articleCategoryId";
	private static final String ARTICLECATEGORIES = "articleCategories";

	@Autowired
	private ArticleCategoryService articleCategoryService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Long localLong = (Long) FreeMarkers.getParameter(ARTICLECATEGORY_ID,
				Long.class, params);
		ArticleCategory localArticleCategory = (ArticleCategory) this.articleCategoryService
				.find(localLong);
		Object localObject;
		if ((localLong != null) && (localArticleCategory == null)) {
			localObject = new ArrayList();
		} else {
			boolean bool = setFreemarker(env, params);
			String str = getFreemarkerCacheRegion(env, params);
			Integer localInteger = getFreemarkerCount(params);
			if (bool)
				localObject = this.articleCategoryService.findParents(
						localArticleCategory, localInteger, str);
			else
				localObject = this.articleCategoryService.findParents(
						localArticleCategory, localInteger);
		}
		setFreemarker(ARTICLECATEGORIES, localObject, env, body);
	}
}