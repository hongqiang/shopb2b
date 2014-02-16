package com.hongqiang.shop.common.template.method;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import com.hongqiang.shop.common.utils.SpringContextHolder;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;

@Component("messageMethod")
public class MessageMethod
  implements TemplateMethodModel
{
  public Object exec(@SuppressWarnings("rawtypes") List arguments)
  {
    if ((arguments != null) && (!arguments.isEmpty()) && (arguments.get(0) != null) && (StringUtils.isNotEmpty(arguments.get(0).toString())))
    {
      String str1 = null;
      String str2 = arguments.get(0).toString();
      if (arguments.size() > 1)
      {
        Object[] arrayOfObject = arguments.subList(1, arguments.size()).toArray();
        str1 = SpringContextHolder.getMessage(str2, arrayOfObject);
      }
      else
      {
        str1 = SpringContextHolder.getMessage(str2, new Object[0]);
      }
      return new SimpleScalar(str1);
    }
    return null;
  }
}