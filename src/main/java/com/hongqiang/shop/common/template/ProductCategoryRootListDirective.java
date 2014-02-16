package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.product.service.ProductCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productCategoryRootListDirective")
public class ProductCategoryRootListDirective extends BaseDirective {
	private static final String PRODUCTCATEGORIES = "productCategories";

	@Autowired
	private ProductCategoryService productCategoryService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList;
		if (bool)
			localList = this.productCategoryService
					.findRoots(localInteger, str);
		else
			localList = this.productCategoryService.findRoots(localInteger);
		setFreemarker(PRODUCTCATEGORIES, localList, env, body);
	}
}