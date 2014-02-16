package com.hongqiang.shop.common.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hongqiang.shop.common.utils.AuthenticationToken;
import com.hongqiang.shop.modules.util.service.RSAService;

public class AuthenticationFilter extends FormAuthenticationFilter {
	private static final String ENPASSWORD = "enPassword";
	private static final String CAPTCHA_ID = "captchaId";
	private static final String CAPTCHA = "captcha";
	private String enPassword = ENPASSWORD;
	private String captchaId = CAPTCHA_ID;
	private String captcha = CAPTCHA;

	@Autowired
	private RSAService rsaService;

	protected org.apache.shiro.authc.AuthenticationToken createToken(
			ServletRequest servletRequest, ServletResponse servletResponse) {
		String str1 = getUsername(servletRequest);
		String str2 = getPassword(servletRequest);
		String str3 = getWebUtils(servletRequest);
		String str4 = getWebUtilsClean(servletRequest);
		boolean bool = isRememberMe(servletRequest);
		String str5 = getHost(servletRequest);
		return new AuthenticationToken(str1, str2, str3, str4, bool, str5);
	}

	protected boolean onAccessDenied(ServletRequest servletRequest,
			ServletResponse servletResponse) {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) servletResponse;
		String str = localHttpServletRequest.getHeader("X-Requested-With");
		if ((str != null) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
			localHttpServletResponse.addHeader("loginStatus", "accessDenied");
			try {
				localHttpServletResponse.sendError(403);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		try {
			return super.onAccessDenied(localHttpServletRequest,
					localHttpServletResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected boolean onLoginSuccess(
			org.apache.shiro.authc.AuthenticationToken token, Subject subject,
			ServletRequest servletRequest, ServletResponse servletResponse) {
		Session localSession = subject.getSession();
		HashMap<Object, Object> localHashMap = new HashMap<Object, Object>();
		Collection<Object> localCollection = localSession.getAttributeKeys();
		Iterator<Object> localIterator = localCollection.iterator();
		while (localIterator.hasNext()) {
			Object localObject = localIterator.next();
			localHashMap.put(localObject,
					localSession.getAttribute(localObject));
		}
		localSession.stop();
		localSession = subject.getSession();
		Iterator<Entry<Object, Object>> localIterator2 = localHashMap
				.entrySet().iterator();
		while (localIterator2.hasNext()) {
			Entry<Object, Object> localObject = (Entry<Object, Object>) localIterator2
					.next();
			localSession.setAttribute(localObject.getKey(),
					localObject.getValue());
		}
		try {
			return super.onLoginSuccess(token, subject, servletRequest,
					servletResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	protected String getPassword(ServletRequest servletRequest) {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) servletRequest;
		String str = this.rsaService.decryptParameter(this.enPassword,
				localHttpServletRequest);
		this.rsaService.removePrivateKey(localHttpServletRequest);
		return str;
	}

	protected String getWebUtils(ServletRequest paramServletRequest) {
		String str = WebUtils
				.getCleanParam(paramServletRequest, this.captchaId);
		if (str == null)
			str = ((HttpServletRequest) paramServletRequest).getSession()
					.getId();
		return str;
	}

	protected String getWebUtilsClean(ServletRequest paramServletRequest) {
		return WebUtils.getCleanParam(paramServletRequest, this.captcha);
	}

	public String getEnPassword() {
		return this.enPassword;
	}

	public void setEnPassword(String enPassword) {
		this.enPassword = enPassword;
	}

	public String getCaptchaId() {
		return this.captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getCaptcha() {
		return this.captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}