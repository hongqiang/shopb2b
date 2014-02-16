package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.entity.Brand;
import com.hongqiang.shop.modules.product.service.BrandService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("brandListDirective")
public class BrandListDirective extends BaseDirective {
	private static final String BRANDS = "brands";

	@Autowired
	private BrandService brandService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList2 = getFreemarkerFilter(params, Brand.class,
				new String[0]);
		List localList3 = getFreemarkerOrder(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.brandService.findList(localInteger, localList2,
					localList3, str);
		else
			localList1 = this.brandService.findList(localInteger, localList2,
					localList3);
		setFreemarker(BRANDS, localList1, env, body);
	}
}