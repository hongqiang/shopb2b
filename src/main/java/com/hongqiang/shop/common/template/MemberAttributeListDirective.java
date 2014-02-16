package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.user.service.MemberAttributeService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("memberAttributeListDirective")
public class MemberAttributeListDirective extends BaseDirective
{
  private static final String MEMBER_ATTRIBUTES = "memberAttributes";

  @Autowired
  private MemberAttributeService memberAttributeService;

  @SuppressWarnings({ "unchecked", "rawtypes" })
public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException
  {
    boolean bool = setFreemarker(env, params);
    String str = getFreemarkerCacheRegion(env, params);
    List localList;
    if (bool)
      localList = this.memberAttributeService.findList(str);
    else
      localList = this.memberAttributeService.findList();
    setFreemarker(MEMBER_ATTRIBUTES, localList, env, body);
  }
}