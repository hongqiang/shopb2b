package com.hongqiang.shop.modules.user.web.shop;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.CookieUtils;
import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Principal;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CartService;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;
import com.hongqiang.shop.modules.util.service.RSAService;

@Controller("shopLoginController")
@RequestMapping({ "${frontPath}/login" })
public class LoginController extends BaseController {

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private RSAService rsaService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CartService cartService;

	@RequestMapping(value = { "/check" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean check() {
		return Boolean.valueOf(this.memberService.isAuthenticated());
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model) {
		Setting setting = SettingUtils.get();
		if ((redirectUrl != null)
				&& (!redirectUrl.equalsIgnoreCase(setting.getSiteUrl()))
				&& (!redirectUrl.startsWith(request.getContextPath() + "/"))
				&& (!redirectUrl.startsWith(setting.getSiteUrl() + "/")))
			redirectUrl = null;
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/login/index";
	}

	@RequestMapping(value = { "/submit" }, method =RequestMethod.POST)
	@ResponseBody
	public Message submit(String captchaId, String captcha, String username,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String password = this.rsaService.decryptParameter("enPassword", request);
		this.rsaService.removePrivateKey(request);
		if (!this.captchaService.isValid(Setting.CaptchaType.memberLogin, captchaId, captcha))
			return Message.error("shop.captcha.invalid", new Object[0]);
		if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(password)))
			return Message.error("shop.common.invalid", new Object[0]);
		Setting setting = SettingUtils.get();
		Member member;
		if ((setting.getIsEmailLogin().booleanValue()) && (username.contains("@"))) {
			List<Member> members = this.memberService.findListByEmail(username);
			if (members.isEmpty())
				member = null;
			else if (members.size() == 1)
				member = (Member) members.get(0);
			else
				return Message.error("shop.login.unsupportedAccount", new Object[0]);
		} else {
			member = this.memberService.findByUsername(username);
		}
		if (member == null)
			return Message.error("shop.login.unknownAccount", new Object[0]);
		if (!member.getIsEnabled().booleanValue())
			return Message.error("shop.login.disabledAccount", new Object[0]);
		if (member.getIsLocked().booleanValue()){
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member)) {
				int minute = setting.getAccountLockTime().intValue();
				if (minute == 0)
					return Message.error("shop.login.lockedAccount", new Object[0]);
				Date lockedDate = member.getLockedDate();
				Date lockingDate = DateUtils.addMinutes(lockedDate, minute);
				if (new Date().after(lockingDate)) {
					member.setLoginFailureCount(Integer.valueOf(0));
					member.setIsLocked(Boolean.valueOf(false));
					member.setLockedDate(null);
					this.memberService.update(member);
				} else {
					return Message.error("shop.login.lockedAccount", new Object[0]);
				}
			} else {
				member.setLoginFailureCount(Integer.valueOf(0));
				member.setIsLocked(Boolean.valueOf(false));
				member.setLockedDate(null);
				this.memberService.update(member);
			}
		}
		if (!DigestUtils.md5Hex(password).equals(member.getPassword())) {
			int count = member.getLoginFailureCount().intValue() + 1;
			if (count >= setting.getAccountLockCount().intValue()) {
				member.setIsLocked(Boolean.valueOf(true));
				member.setLockedDate(new Date());
			}
			member.setLoginFailureCount(Integer.valueOf(count));
			this.memberService.update(member);
			if (ArrayUtils.contains(setting.getAccountLockTypes(), Setting.AccountLockType.member))
				return Message.error("shop.login.accountLockCount", new Object[] { setting.getAccountLockCount() });
			return Message.error("shop.login.incorrectCredentials", new Object[0]);
		}
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setLoginFailureCount(Integer.valueOf(0));
		this.memberService.update(member);
		Cart cart = this.cartService.getCurrent();
		if ((cart != null) && (cart.getMember() == null)) {
			this.cartService.merge(member, cart);
			CookieUtils.removeCookie(request, response, "cartId");
			CookieUtils.removeCookie(request, response, "cartKey");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		Enumeration enumer = session.getAttributeNames();
		while (enumer.hasMoreElements()) {
			String attribute = (String) enumer.nextElement();
			map.put(attribute,session.getAttribute(attribute));
		}
		session.invalidate();
		session = request.getSession();
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> pairs = (Entry<String, Object>) iterator.next();
			session.setAttribute((String)pairs.getKey(),pairs.getValue());
		}
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(member.getId(), username));
		CookieUtils.setCookie(request, response, "username",member.getUsername());
		System.out.println("we submit, message = "+SHOP_SUCCESS.toString());
		return  SHOP_SUCCESS;
	}
}