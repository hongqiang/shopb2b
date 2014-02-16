package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.entity.Tag;
import com.hongqiang.shop.modules.product.service.TagService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("tagListDirective")
public class TagListDirective extends BaseDirective {
	private static final String TAGS = "tags";

	@Autowired
	private TagService tagService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		Integer localInteger = getFreemarkerCount(params);
		List localList2 = getFreemarkerFilter(params, Tag.class, new String[0]);
		List localList3 = getFreemarkerOrder(params, new String[0]);
		List localList1;
		if (bool)
			localList1 = this.tagService.findList(localInteger, localList2,
					localList3, str);
		else
			localList1 = this.tagService.findList(localInteger, localList2,
					localList3);
		setFreemarker(TAGS, localList1, env, body);
	}
}