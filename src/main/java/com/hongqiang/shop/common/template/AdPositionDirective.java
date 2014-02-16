package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.hongqiang.shop.website.entity.AdPosition;
import com.hongqiang.shop.website.service.AdPositionService;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("adPositionDirective")
public class AdPositionDirective extends BaseDirective {
	private static final String AP_POSITION = "adPosition";

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Autowired
	private AdPositionService adPositionService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Long localLong = getFreemarkerLong(params);
		boolean bool = setFreemarker(env, params);
		String str = getFreemarkerCacheRegion(env, params);
		AdPosition localAdPosition;
		if (bool)
			localAdPosition = this.adPositionService.find(localLong, str);
		else
			localAdPosition = (AdPosition) this.adPositionService
					.find(localLong);
		if (body != null)
			setFreemarker(AP_POSITION, localAdPosition, env, body);
		else if ((localAdPosition != null)
				&& (localAdPosition.getTemplate() != null))
			try {
				HashMap localHashMap = new HashMap();
				localHashMap.put(AP_POSITION, localAdPosition);
				Writer localWriter = env.getOut();
				new Template("adTemplate", new StringReader(
						localAdPosition.getTemplate()),
						this.freeMarkerConfigurer.getConfiguration()).process(
						localHashMap, localWriter);
			} catch (Exception localException1) {
				localException1.printStackTrace();
			}
	}
}