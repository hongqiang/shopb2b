package com.hongqiang.shop.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AccessDeniedFilter
  implements Filter
{
  private static final String ADDCSS_DENIED = "Access denied!";

  public void init(FilterConfig filterConfig)
  {
  }

  public void destroy()
  {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
  {
    HttpServletResponse localHttpServletResponse = (HttpServletResponse)servletResponse;
    try {
		localHttpServletResponse.sendError(403, ADDCSS_DENIED);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}