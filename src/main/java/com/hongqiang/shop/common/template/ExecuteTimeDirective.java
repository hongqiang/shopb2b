package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.hongqiang.shop.common.interceptor.ExecuteTimeInterceptor;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("executeTimeDirective")
public class ExecuteTimeDirective extends BaseDirective {
	private static final String EXECUTE_TIME = "executeTime";

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		RequestAttributes localRequestAttributes = RequestContextHolder
				.currentRequestAttributes();
		if (localRequestAttributes != null) {
			Long localLong = (Long) localRequestAttributes.getAttribute(
					ExecuteTimeInterceptor.EXECUTE_TIME_ATTRIBUTE_NAME, 0);
			if (localLong != null)
				setFreemarker(EXECUTE_TIME, localLong, env, body);
		}
	}
}