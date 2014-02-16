package com.hongqiang.shop.modules.utils;

import java.io.Serializable;

public class Template
  implements Serializable
{

public enum Type
{
  page, mail, print;
}

  private static final long serialVersionUID = -517540800437045215L;
  private String id;
  private Type type;
  private String name;
  private String templatePath;
  private String staticPath;
  private String description;

  public String getId()
  {
    return this.id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public Type getType()
  {
    return this.type;
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTemplatePath()
  {
    return this.templatePath;
  }

  public void setTemplatePath(String templatePath)
  {
    this.templatePath = templatePath;
  }

  public String getStaticPath()
  {
    return this.staticPath;
  }

  public void setStaticPath(String staticPath)
  {
    this.staticPath = staticPath;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }
}