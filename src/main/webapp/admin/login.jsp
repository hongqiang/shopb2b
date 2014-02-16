<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.hongqiang.shop.common.utils.Setting"%>
<%@page import="com.hongqiang.shop.common.utils.SettingUtils"%>
<%@page import="com.hongqiang.shop.common.utils.SpringContextHolder"%>
<%@page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@page import="java.util.UUID"%>
<%@page import="com.hongqiang.shop.common.utils.Setting.AccountLockType"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.hongqiang.shop.common.utils.Setting.CaptchaType"%>
<%@page import="java.security.interfaces.RSAPublicKey"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="com.hongqiang.shop.modules.util.service.RSAService"%>
<%@page import="com.hongqiang.shop.common.config.Global"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
String base = request.getContextPath();
String captchaId = UUID.randomUUID().toString();
ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
Setting setting = SettingUtils.get();
if (applicationContext != null) {
%>
<shiro:authenticated>
<%
response.sendRedirect(base + Global.getAdminPath()+"/common/main.jhtml");
%>
</shiro:authenticated>
<%
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<%
if (applicationContext != null) {
	RSAService rsaService = SpringContextHolder.getBean("rsaServiceImpl", RSAService.class);
	RSAPublicKey publicKey = rsaService.generateKey(request);
	String modulus = Base64.encodeBase64String(publicKey.getModulus().toByteArray());
	String exponent = Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray());
	
	String message = null;
	String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	if (loginFailure != null) {
		if (loginFailure.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
			message = "admin.captcha.invalid";
		} else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
			message = "admin.login.unknownAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
			message = "admin.login.disabledAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
			message = "admin.login.lockedAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.admin)) {
				message = "admin.login.accountLockCount";
			} else {
				message = "admin.login.incorrectCredentials";
			}
		} else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
			message = "admin.login.authentication";
		}
	}
%>
<title><%=SpringContextHolder.getMessage("admin.login.title", new Object[0])%> - Powered By HONGQIANG_SHOP</title>
<meta http-equiv="expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="<%=base%>/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=base%>/resources/admin/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=base%>/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/jsbn.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/prng4.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/rng.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/rsa.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/base64.js"></script>
<script type="text/javascript" src="<%=base%>/resources/admin/js/common.js"></script>
<script type="text/javascript">
	$().ready( function() {
		
		var $loginForm = $("#loginForm");
		var $enPassword = $("#enPassword");
		var $username = $("#username");
		var $password = $("#password");
		var $captcha = $("#captcha");
		var $captchaImage = $("#captchaImage");
		var $isRememberUsername = $("#isRememberUsername");
		
		// 记住用户名
		if(getCookie("adminUsername") != null) {
			$isRememberUsername.prop("checked", true);
			$username.val(getCookie("adminUsername"));
			$password.focus();
		} else {
			$isRememberUsername.prop("checked", false);
			$username.focus();
		}
		
		// 更换验证码
		$captchaImage.click( function() {
			$captchaImage.attr("src", "<%=base+ Global.getAdminPath()%>/common/captcha.jhtml?captchaId=<%=captchaId%>&timestamp=" + (new Date()).valueOf());
		});
		
		// 表单验证、记住用户名
		$loginForm.submit( function() {
			if ($username.val() == "") {
				$.message("warn", "<%=SpringContextHolder.getMessage("admin.login.usernameRequired", new Object[0])%>");
				return false;
			}
			if ($password.val() == "") {
				$.message("warn", "<%=SpringContextHolder.getMessage("admin.login.passwordRequired", new Object[0])%>");
				return false;
			}
			if ($captcha.val() == "") {
				$.message("warn", "<%=SpringContextHolder.getMessage("admin.login.captchaRequired", new Object[0])%>");
				return false;
			}
			
			if ($isRememberUsername.prop("checked")) {
				addCookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
			} else {
				removeCookie("adminUsername");
			}
			
			var rsaKey = new RSAKey();
			rsaKey.setPublic(b64tohex("<%=modulus%>"), b64tohex("<%=exponent%>"));
			var enPassword = hex2b64(rsaKey.encrypt($password.val()));
			$enPassword.val(enPassword);
		});
		
		<%if (message != null) {%>
			$.message("error", "<%=SpringContextHolder.getMessage(message, new Object[]{setting.getAccountLockCount()})%>");
		<%}%>
	});
</script>
<%} else {%>
<title>提示信息 - Powered By HONGQIANG_SHOP</title>
<meta http-equiv="expires" content="0" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="author" content="HONGQIANG_SHOP Team" />
<meta name="copyright" content="HONGQIANG_SHOP" />
<link href="<%=base%>/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link href="<%=base%>/resources/admin/css/login.css" rel="stylesheet" type="text/css" />
<%}%>
</head>
<body>
	<%if (applicationContext != null) {%>
		<div class="login">
			<form id="loginForm" action="login.jsp" method="post">
				<input type="hidden" id="enPassword" name="enPassword" />
				<%if (ArrayUtils.contains(setting.getCaptchaTypes(), CaptchaType.adminLogin)) {%>
					<input type="hidden" name="captchaId" value="<%=captchaId%>" />
				<%}%>
				<table>
					<tr>
						<td width="190" rowspan="2" align="center" valign="bottom">
							<img src="<%=base%>/resources/admin/images/login_logo.gif" alt="HONGQIANG_SHOP" />
						</td>
						<th>
							<%=SpringContextHolder.getMessage("admin.login.username", new Object[0])%>:
						</th>
						<td>
							<input type="text" id="username" name="username" class="text" maxlength="20" />
						</td>
					</tr>
					<tr>
						<th>
							<%=SpringContextHolder.getMessage("admin.login.password", new Object[0])%>:
						</th>
						<td>
							<input type="password" id="password" class="text" maxlength="20" autocomplete="off" />
						</td>
					</tr>
					<%if (ArrayUtils.contains(setting.getCaptchaTypes(), CaptchaType.adminLogin)) {%>
						<tr>
							<td>
								&nbsp;
							</td>
							<th>
								<%=SpringContextHolder.getMessage("admin.captcha.name", new Object[0])%>:
							</th>
							<td>
								<input type="text" id="captcha" name="captcha" class="text captcha" maxlength="4" autocomplete="off" /><img id="captchaImage" class="captchaImage" src="<%=base+ Global.getAdminPath()%>/common/captcha.jhtml?captchaId=<%=captchaId%>" title="<%=SpringContextHolder.getMessage("admin.captcha.imageTitle", new Object[0])%>" />
							</td>
						</tr>
					<%}%>
					<tr>
						<td>
							&nbsp;
						</td>
						<th>
							&nbsp;
						</th>
						<td>
							<label>
								<input type="checkbox" id="isRememberUsername" value="true" />
								<%=SpringContextHolder.getMessage("admin.login.rememberUsername", new Object[0])%>:
							</label>
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<th>
							&nbsp;
						</th>
						<td>
							<input type="button" class="homeButton" value="" onclick="location.href='<%=base%>/'" /><input type="submit" class="loginButton" value="<%=SpringContextHolder.getMessage("admin.login.login", new Object[0])%>" />
						</td>
					</tr>
				</table>
				<div class="powered">COPYRIGHT © 2013-2014 SHIJIHONGQIANG ALL RIGHTS RESERVED.</div>
				<div class="link">
					<a href="<%=base%>/"><%=SpringContextHolder.getMessage("admin.login.home", new Object[0])%></a> |
					<a href="http://www.shopxx.net"><%=SpringContextHolder.getMessage("admin.login.official", new Object[0])%></a> |
					<a href="http://bbs.shopxx.net"><%=SpringContextHolder.getMessage("admin.login.bbs", new Object[0])%></a> |
					<a href="http://www.shopxx.net/about.html"><%=SpringContextHolder.getMessage("admin.login.about", new Object[0])%></a> |
					<a href="http://www.shopxx.net/contact.html"><%=SpringContextHolder.getMessage("admin.login.contact", new Object[0])%></a> |
					<a href="http://www.shopxx.net/license.html"><%=SpringContextHolder.getMessage("admin.login.license", new Object[0])%></a>
				</div>
			</form>
		</div>
	<%} else {%>
		<fieldset>
			<legend>系统出现异常</legend>
			<p>请检查HONGQIANG_SHOP程序是否已正确安装 [<a href="<%=base%>/install/">点击此处进行安装</a>]</p>
			<p>
				<strong>提示: HONGQIANG_SHOP安装完成后必须重新启动WEB服务器</strong>
			</p>
		</fieldset>
	<%}%>
</body>
</html>