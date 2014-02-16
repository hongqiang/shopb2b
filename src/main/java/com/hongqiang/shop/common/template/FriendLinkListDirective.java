package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.website.entity.FriendLink;
import com.hongqiang.shop.website.service.FriendLinkService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

@Component("friendLinkListDirective")
public class FriendLinkListDirective extends BaseDirective {
	private static final String FRIENDlINKS = "friendLinks";

	@Autowired
	private FriendLinkService friendLinkService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateModelException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList2 = getFreemarkerFilter(params, FriendLink.class,
				new String[0]);
		List localList3 = getFreemarkerOrder(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.friendLinkService.findList(localInteger,
					localList2, localList3, str);
		else
			localList1 = this.friendLinkService.findList(localInteger,
					localList2, localList3);
		try {
			setFreemarker(FRIENDlINKS, localList1, env, body);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}