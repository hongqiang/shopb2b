package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.common.utils.FreeMarkers;
import com.hongqiang.shop.website.entity.Seo;
import com.hongqiang.shop.website.service.SeoService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("seoDirective")
public class SeoDirective extends BaseDirective
{
  private static final String TYPE = "type";
  private static final String SEO = "seo";

  @Autowired
  private SeoService seoService;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)throws TemplateException, IOException
  {
    Seo.Type localType = (Seo.Type)FreeMarkers.getParameter(TYPE, Seo.Type.class, params);
    boolean bool = setFreemarker(env, params);
    String str = getFreemarkerCacheRegion(env, params);
    Seo localSeo;
    if (bool)
      localSeo = this.seoService.find(localType, str);
    else
      localSeo = this.seoService.find(localType);
    setFreemarker(SEO, localSeo, env, body);
  }
}