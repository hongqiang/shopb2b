package com.hongqiang.shop.common.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.hongqiang.shop.common.utils.Message;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("flashMessageDirective")
public class FlashMessageDirective extends BaseDirective
{
  public static final String FLASH_MESSAGE_ATTRIBUTE_NAME = FlashMessageDirective.class.getName() + ".FLASH_MESSAGE";
  private static final String FLASH_MESSAGE = "flashMessage";

  public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException
  {
    RequestAttributes localRequestAttributes = RequestContextHolder.currentRequestAttributes();
    if (localRequestAttributes != null)
    {
      Message localMessage = (Message)localRequestAttributes.getAttribute(FLASH_MESSAGE_ATTRIBUTE_NAME, 0);
      if (body != null)
      {
        setFreemarker(FLASH_MESSAGE, localMessage, env, body);
      }
      else if (localMessage != null)
      {
        Writer localWriter = env.getOut();
        localWriter.write("$.message(\"" + localMessage.getType() + "\", \"" + localMessage.getContent() + "\");");
      }
    }
  }
}