package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.modules.account.service.PromotionService;
import com.hongqiang.shop.modules.entity.Promotion;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("promotionListDirective")
public class PromotionListDirective extends BaseDirective {
	private static final String BEGIN = "hasBegun";
	private static final String END = "hasEnded";
	private static final String PROMOTIONS = "promotions";

	@Autowired
	private PromotionService promotionService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Boolean localBoolean1 = (Boolean) FreeMarkers.getParameter(BEGIN,
				Boolean.class, params);
		Boolean localBoolean2 = (Boolean) FreeMarkers.getParameter(END,
				Boolean.class, params);
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList2 = getFreemarkerFilter(params, Promotion.class,
				new String[0]);
		List localList3 = getFreemarkerOrder(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.promotionService.findList(localBoolean1,
					localBoolean2, localInteger, localList2, localList3, str);
		else
			localList1 = this.promotionService.findList(localBoolean1,
					localBoolean2, localInteger, localList2, localList3);
		setFreemarker(PROMOTIONS, localList1, env, body);
	}
}