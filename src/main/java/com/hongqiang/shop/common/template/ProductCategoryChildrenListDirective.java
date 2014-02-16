package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.entity.ProductCategory;
import com.hongqiang.shop.modules.product.service.ProductCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("productCategoryChildrenListDirective")
public class ProductCategoryChildrenListDirective extends BaseDirective {
	private static final String PRODUCTCATEGORY_ID = "productCategoryId";
	private static final String PRODUCTCATEGORIES = "productCategories";

	@Autowired
	private ProductCategoryService productCategoryService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Long localLong = (Long) FreeMarkers.getParameter(PRODUCTCATEGORY_ID,
				Long.class, params);
		ProductCategory localProductCategory = (ProductCategory) this.productCategoryService
				.find(localLong);
		Object localObject;
		if ((localLong != null) && (localProductCategory == null)) {
			localObject = new ArrayList();
		} else {
			boolean bool = setFreemarker(env, params);
			String str = getFreemarkerCacheRegion(env, params);
			Integer localInteger = getFreemarkerCount(params);
			if (bool)
				localObject = this.productCategoryService.findChildren(
						localProductCategory, localInteger, str);
			else
				localObject = this.productCategoryService.findChildren(
						localProductCategory, localInteger);
		}
		setFreemarker(PRODUCTCATEGORIES, localObject, env, body);
	}
}