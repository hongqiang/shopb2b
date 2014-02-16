package com.hongqiang.shop.modules.util.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.modules.utils.Template;

@Service
public class TemplateServiceImpl
  implements TemplateService, ServletContextAware
{
  private ServletContext servletContext;

  @Value("${template.loader_path}")
  private String[] loaderpath;

  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  @Cacheable({"template"})
  public List<Template> getAll()
  {
    try
    {
      File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
      Document localDocument = new SAXReader().read(localFile);
      ArrayList<Template> localArrayList = new ArrayList<Template>();
      @SuppressWarnings("unchecked")
	List<Template> localList = localDocument.selectNodes("/shophq/template");
      Iterator<Template> localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Element localElement = (Element)localIterator.next();
        Template localTemplate = compTemplate(localElement);
        localArrayList.add(localTemplate);
      }
      return localArrayList;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }

  @Cacheable({"template"})
  public List<Template> getList(Template.Type type)
  {
    if (type != null)
      try
      {
        File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
        Document localDocument = new SAXReader().read(localFile);
        ArrayList<Template> localArrayList = new ArrayList<Template>();
        @SuppressWarnings("unchecked")
		List<Template> localList = localDocument.selectNodes("/shophq/template[@type='" + type + "']");
        Iterator<Template> localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          Element localElement = (Element)localIterator.next();
          Template localTemplate = compTemplate(localElement);
          localArrayList.add(localTemplate);
        }
        return localArrayList;
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
        return null;
      }
    return getAll();
  }

  @Cacheable({"template"})
  public Template get(String id)
  {
    try
    {
      File localFile = new ClassPathResource(CommonAttributes.HQ_SHOP_XML_PATH).getFile();
      Document localDocument = new SAXReader().read(localFile);
      Element localElement = (Element)localDocument.selectSingleNode("/shophq/template[@id='" + id + "']");
      Template localTemplate = compTemplate(localElement);
      return localTemplate;
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    return null;
  }

  public String read(String id)
  {
    Template localTemplate = get(id);
    return read(localTemplate);
  }

  public String read(Template template)
  {
    String str1 = this.servletContext.getRealPath(this.loaderpath[0] + template.getTemplatePath());
    File localFile = new File(str1);
    String str2 = null;
    try
    {
      str2 = FileUtils.readFileToString(localFile, "UTF-8");
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return str2;
  }

  public void write(String id, String content)
  {
    Template localTemplate = get(id);
    write(localTemplate, content);
  }

  public void write(Template template, String content)
  {
    String str = this.servletContext.getRealPath(this.loaderpath[0] + template.getTemplatePath());
    File localFile = new File(str);
    try
    {
      FileUtils.writeStringToFile(localFile, content, "UTF-8");
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  private Template compTemplate(Element paramElement)
  {
    String str1 = paramElement.attributeValue("id");
    String str2 = paramElement.attributeValue("type");
    String str3 = paramElement.attributeValue("name");
    String str4 = paramElement.attributeValue("templatePath");
    String str5 = paramElement.attributeValue("staticPath");
    String str6 = paramElement.attributeValue("description");
    Template localTemplate = new Template();
    localTemplate.setId(str1);
    localTemplate.setType(Template.Type.valueOf(str2));
    localTemplate.setName(str3);
    localTemplate.setTemplatePath(str4);
    localTemplate.setStaticPath(str5);
    localTemplate.setDescription(str6);
    return localTemplate;
  }
}