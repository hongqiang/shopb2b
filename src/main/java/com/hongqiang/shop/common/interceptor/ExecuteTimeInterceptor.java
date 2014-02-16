package com.hongqiang.shop.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter
{
  private static final Logger logger = LoggerFactory.getLogger(ExecuteTimeInterceptor.class);
  private static final String START_TIME_ATTRIBUTE = ExecuteTimeInterceptor.class.getName() + ".START_TIME";
  public static final String EXECUTE_TIME_ATTRIBUTE_NAME = ExecuteTimeInterceptor.class.getName() + ".EXECUTE_TIME";
  private static final String REDIRECT = "redirect:";

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
  {
    Long localLong = (Long)request.getAttribute(START_TIME_ATTRIBUTE);
    if (localLong == null)
    {
      localLong = Long.valueOf(System.currentTimeMillis());
      request.setAttribute(START_TIME_ATTRIBUTE, localLong);
    }
    return true;
  }

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
  {
    Long localLong1 = (Long)request.getAttribute(EXECUTE_TIME_ATTRIBUTE_NAME);
    if (localLong1 == null)
    {
      Long startTime = (Long)request.getAttribute(START_TIME_ATTRIBUTE);
      Long localLong2 = Long.valueOf(System.currentTimeMillis());
      localLong1 = Long.valueOf(localLong2.longValue() - startTime.longValue());
      request.setAttribute(START_TIME_ATTRIBUTE, startTime);
    }
    if (modelAndView != null)
    {
      String viewName = modelAndView.getViewName();
      if (!StringUtils.startsWith(viewName, REDIRECT))
        modelAndView.addObject(EXECUTE_TIME_ATTRIBUTE_NAME, localLong1);
    }
    if (logger.isDebugEnabled())
      logger.debug("[" + handler + "] executeTime: " + localLong1 + "ms");
  }
}