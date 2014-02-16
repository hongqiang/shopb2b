package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.content.service.ArticleCategoryService;
import com.hongqiang.shop.modules.content.service.ArticleService;
import com.hongqiang.shop.modules.entity.Article;
import com.hongqiang.shop.modules.entity.ArticleCategory;
import com.hongqiang.shop.modules.product.service.TagService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("articleListDirective")
public class ArticleListDirective extends BaseDirective {
	private static final String ARTICLECATEGORY_ID = "articleCategoryId";
	private static final String TAG_IDS = "tagIds";
	private static final String ARTICLES = "articles";

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleCategoryService articleCategoryService;

	@Autowired
	private TagService tagService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		 Set<Map.Entry> set = params.entrySet();
	        for (Iterator<Map.Entry> it = set.iterator(); it.hasNext();) {
	            Map.Entry entry = (Map.Entry) it.next();
	            System.out.println(entry.getKey() + "--->" + entry.getValue());
	        }
		
		Long localLong = (Long) FreeMarkers.getParameter(ARTICLECATEGORY_ID,
				Long.class, params);
		Long[] arrayOfLong = (Long[])FreeMarkers.getParameter(TAG_IDS,Long[].class, params);
		ArticleCategory localArticleCategory = (ArticleCategory) this.articleCategoryService
				.find(localLong);
		List localList1 = this.tagService.findList(arrayOfLong);
		Object localObject;
		if (((localLong != null) && (localArticleCategory == null))
				|| ((arrayOfLong != null) && (localList1.isEmpty()))) {
			localObject = new ArrayList();
		} else {
			boolean bool = setFreemarker(env, params);
			String str = getFreemarkerCacheRegion(env, params);
			Integer localInteger = getFreemarkerCount(params);
			List localList2 = getFreemarkerFilter(params, Article.class,
					new String[0]);
			List localList3 = getFreemarkerOrder(params, new String[0]);
			if (bool)
				localObject = this.articleService.findList(
						localArticleCategory, localList1, localInteger,
						localList2, localList3, str);
			else
				localObject = this.articleService.findList(
						localArticleCategory, localList1, localInteger,
						localList2, localList3);
		}
		setFreemarker(ARTICLES, localObject, env, body);
	}
}