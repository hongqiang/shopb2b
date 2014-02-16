package com.hongqiang.shop.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;

@Component("siteStatusFilter")
public class SiteStatusFilter extends OncePerRequestFilter
{
  private static final String[] ADMIN_PATH = { Global.getAdminPath()+"/**" };
  private static final String COMMON_URL = Global.getAdminPath()+"/common/site_close.jhtml";
  private static AntPathMatcher antPathMatcher = new AntPathMatcher();
  private String[] ignoreUrlPatterns = ADMIN_PATH;
  private String redirectUrl = COMMON_URL;

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
  {
    Setting localSetting = SettingUtils.get();
    if (localSetting.getIsSiteEnabled().booleanValue())
    {
      try {
		filterChain.doFilter(request, response);
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ServletException e) {
		e.printStackTrace();
	}
    }
    else
    {
      String str1 = request.getServletPath();
      if (str1.equals(this.redirectUrl))
      {
        try {
			filterChain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
        return;
      }
      if (this.ignoreUrlPatterns != null)
        for (String str2 : this.ignoreUrlPatterns)
        {
          if (!antPathMatcher.match(str2, str1))
            continue;
          try {
			filterChain.doFilter(request, response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
          return;
        }
      try {
		response.sendRedirect(request.getContextPath() + this.redirectUrl);
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
  }

  public String[] getIgnoreUrlPatterns()
  {
    return this.ignoreUrlPatterns;
  }

  public void setIgnoreUrlPatterns(String[] ignoreUrlPatterns)
  {
    this.ignoreUrlPatterns = ignoreUrlPatterns;
  }

  public String getRedirectUrl()
  {
    return this.redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl)
  {
    this.redirectUrl = redirectUrl;
  }
}