package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.website.entity.Navigation;
import com.hongqiang.shop.website.service.NavigationService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("navigationListDirective")
public class NavigationListDirective extends BaseDirective {
	private static final String NAVIGATIONS = "navigations";

	@Autowired
	private NavigationService navigationService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList2 = getFreemarkerFilter(params, Navigation.class,
				new String[0]);
		List localList3 = getFreemarkerOrder(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.navigationService.findList(localInteger,
					localList2, localList3, str);
		else
			localList1 = this.navigationService.findList(localInteger,
					localList2, localList3);
		setFreemarker(NAVIGATIONS, localList1, env, body);
	}
}