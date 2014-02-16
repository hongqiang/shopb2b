package com.hongqiang.shop.common.interceptor;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hongqiang.shop.common.config.Global;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

public class MemberInterceptor extends HandlerInterceptorAdapter {
	private static final String REDIRECT = "redirect:";
	private static final String REDIRECT_URL = "redirectUrl";
	private static final String MEMBER = "member";
	private static final String LOGIN_URL = Global.getFrontPath()+ "/login.jhtml";
	private String loginUrl = LOGIN_URL;

	@Value("${url_escaping_charset}")
	private String url_escaping_charset;

	@Autowired
	private MemberService memberService;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws IOException {
		HttpSession httpSession = request.getSession();
		Principal principal = (Principal) httpSession.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (principal != null)
			return true;
		String header = request.getHeader("X-Requested-With");
		if ((header != null) && (header.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String url = request.getQueryString() != null ? 
					request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
			response.sendRedirect(request.getContextPath() + this.loginUrl+ "?" + REDIRECT_URL + "="
					+ URLEncoder.encode(url, this.url_escaping_charset));
		} else {
			response.sendRedirect(request.getContextPath() + this.loginUrl);
		}
		return false;
	}

	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) {
		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (!StringUtils.startsWith(viewName, REDIRECT))
				modelAndView.addObject(MEMBER, this.memberService.getCurrent());
		}
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
}