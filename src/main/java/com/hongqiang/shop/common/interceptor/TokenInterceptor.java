package com.hongqiang.shop.common.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hongqiang.shop.common.utils.CookieUtils;

public class TokenInterceptor extends HandlerInterceptorAdapter
{
  private static final String COOKIE_TOKEN = "token";
  private static final String HEADER_TOKEN = "token";
  private static final String PARAMETER_TOKEN = "token";
  private static final String ERROR_TOKEN = "Bad or missing token!";

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
  {
    String str1 = CookieUtils.getCookie(request, COOKIE_TOKEN);
    if (request.getMethod().equalsIgnoreCase("POST"))
    {
      String str2 = request.getHeader("X-Requested-With");
      if ((str2 != null) && (str2.equalsIgnoreCase("XMLHttpRequest")))
      {
        if ((str1 != null) && (str1.equals(request.getHeader(HEADER_TOKEN))))
          return true;
        response.addHeader("tokenStatus", "accessDenied");
      }
      else if ((str1 != null) && (str1.equals(request.getParameter(PARAMETER_TOKEN))))
      {
        return true;
      }
      if (str1 == null)
      {
        str1 = UUID.randomUUID().toString();
        CookieUtils.setCookie(request, response, COOKIE_TOKEN, str1);
      }
      try {
		response.sendError(403, ERROR_TOKEN);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      return false;
    }
    if (str1 == null)
    {
      str1 = UUID.randomUUID().toString();
      CookieUtils.setCookie(request, response, COOKIE_TOKEN, str1);
    }
    request.setAttribute(PARAMETER_TOKEN, str1);
    return true;
  }
}