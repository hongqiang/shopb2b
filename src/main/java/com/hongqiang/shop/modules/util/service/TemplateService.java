package com.hongqiang.shop.modules.util.service;

import java.util.List;

import com.hongqiang.shop.modules.utils.Template;

public interface TemplateService
{
  public List<Template> getAll();

  public List<Template> getList(Template.Type paramType);

  public Template get(String paramString);

  public String read(String paramString);

  public String read(Template paramTemplate);

  public void write(String paramString1, String paramString2);

  public void write(Template paramTemplate, String paramString);
}