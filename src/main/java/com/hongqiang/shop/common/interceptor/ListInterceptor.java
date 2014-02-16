package com.hongqiang.shop.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hongqiang.shop.common.utils.CookieUtils;

public class ListInterceptor extends HandlerInterceptorAdapter
{
  private static final String REDIRECT = "redirect:";
  private static final String LIST_QUERY = "listQuery";

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
  {
    if ((modelAndView != null) && (modelAndView.isReference()))
    {
      String str1 = modelAndView.getViewName();
      if (StringUtils.startsWith(str1, REDIRECT))
      {
        String str2 = CookieUtils.getCookie(request, LIST_QUERY);
        if (StringUtils.isNotEmpty(str2))
        {
          if (StringUtils.startsWith(str2, "?"))
            str2 = str2.substring(1);
          if (StringUtils.contains(str1, "?"))
            modelAndView.setViewName(str1 + "&" + str2);
          else
            modelAndView.setViewName(str1 + "?" + str2);
          CookieUtils.removeCookie(request, response, LIST_QUERY);
        }
      }
    }
  }
}