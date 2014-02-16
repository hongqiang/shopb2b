package com.hongqiang.shop.common.interceptor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hongqiang.shop.modules.user.service.AdminService;
import com.hongqiang.shop.modules.util.service.LogConfigService;
import com.hongqiang.shop.modules.utils.LogConfig;
import com.hongqiang.shop.website.entity.Log;
import com.hongqiang.shop.website.service.LogService;

public class LogInterceptor extends HandlerInterceptorAdapter
{
  private static final String[] IGNORE_PARAMETERS = { "password", "rePassword", "currentPassword" };
  private static AntPathMatcher antPathMatcher = new AntPathMatcher();
  private String[] ignoreParameters = IGNORE_PARAMETERS;

  @Autowired
  private LogConfigService logConfigService;

  @Autowired
  private LogService logService;

  @Autowired
  private AdminService adminService;

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
  {
    List<LogConfig> localList = this.logConfigService.getAll();
    if (localList != null)
    {
      String str1 = request.getServletPath();
      Iterator<LogConfig> localIterator1 = localList.iterator();
      while (localIterator1.hasNext())
      {
        LogConfig localLogConfig = (LogConfig)localIterator1.next();
        if (!antPathMatcher.match(localLogConfig.getUrlPattern(), str1))
          continue;
        String str2 = this.adminService.getCurrentUsername();
        String str3 = localLogConfig.getOperation();
        String str4 = str2;
        String str5 = (String)request.getAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
        String str6 = request.getRemoteAddr();
        request.removeAttribute(Log.LOG_CONTENT_ATTRIBUTE_NAME);
        StringBuffer localStringBuffer = new StringBuffer();
        @SuppressWarnings("unchecked")
		Map<String,String[]> localMap = request.getParameterMap();
        if (localMap != null)
        {
          Iterator<Entry<String, String[]>> localIterator2 = localMap.entrySet().iterator();
          while (localIterator2.hasNext())
          {
        	  Entry<String, String[]> pairs = (Entry<String, String[]>)localIterator2.next();
            String str7 = (String)pairs.getKey();
            if (ArrayUtils.contains(this.ignoreParameters, str7))
              continue;
            String[] arrayOfString1 = (String[])pairs.getValue();
            if (arrayOfString1 == null)
              continue;
            for (String str8 : arrayOfString1)
              localStringBuffer.append(str7 + " = " + str8 + "\n");
          }
        }
        Log log = new Log();
        log.setOperation(str3);
        log.setOperator(str4);
        log.setContent(str5);
        log.setParameter(localStringBuffer.toString());
        log.setIp(str6);
        this.logService.save(log);
        break;
      }
    }
  }

  public String[] getIgnoreParameters()
  {
    return this.ignoreParameters;
  }

  public void setIgnoreParameters(String[] ignoreParameters)
  {
    this.ignoreParameters = ignoreParameters;
  }
}