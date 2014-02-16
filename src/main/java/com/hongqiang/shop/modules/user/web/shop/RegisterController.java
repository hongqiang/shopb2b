package com.hongqiang.shop.modules.user.web.shop;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
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
import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CartService;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.Cart;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.MemberAttribute;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberAttributeService;
import com.hongqiang.shop.modules.user.service.MemberRankService;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;
import com.hongqiang.shop.modules.util.service.RSAService;

@Controller("shopRegisterController")
@RequestMapping({ "${frontPath}/register" })
public class RegisterController extends BaseController {

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private RSAService rsaService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRankService memberRankService;

	@Autowired
	private MemberAttributeService memberAttributeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private CartService cartService;

	@RequestMapping(value = { "/check_username" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username))
			return false;
		return (!this.memberService.usernameDisabled(username))
				&& (!this.memberService.usernameExists(username));
	}

	@RequestMapping(value = { "/check_email" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email))
			return false;
		return !this.memberService.emailExists(email);
	}

	@RequestMapping(method = RequestMethod.GET )
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/register/index";
	}

	@RequestMapping(value = { "/submit" }, method = RequestMethod.POST)
	@ResponseBody
	public Message submit(String captchaId, String captcha, String username,
			String email, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String str = this.rsaService.decryptParameter("enPassword", request);
		this.rsaService.removePrivateKey(request);
		if (!this.captchaService.isValid(Setting.CaptchaType.memberRegister,
				captchaId, captcha))
			return Message.error("shop.captcha.invalid", new Object[0]);
		Setting localSetting = SettingUtils.get();
		if (!localSetting.getIsRegisterEnabled().booleanValue())
			return Message.error("shop.register.disabled", new Object[0]);
		if ((!beanValidator(Member.class, "username", username,
				new Class[] { BaseEntity.Save.class }))
				|| (!beanValidator(Member.class, "password", str,
						new Class[] { BaseEntity.Save.class }))
				|| (!beanValidator(Member.class, "email", email,
						new Class[] { BaseEntity.Save.class }))) {
			return Message.error("shop.common.invalid", new Object[0]);
		}
		if ((username.length() < localSetting.getUsernameMinLength().intValue())
				|| (username.length() > localSetting.getUsernameMaxLength()
						.intValue()))
			return Message.error("shop.common.invalid", new Object[0]);
		if ((str.length() < localSetting.getPasswordMinLength().intValue())
				|| (str.length() > localSetting.getPasswordMaxLength()
						.intValue()))
			return Message.error("shop.common.invalid", new Object[0]);
		if ((this.memberService.usernameDisabled(username))
				|| (this.memberService.usernameExists(username)))
			return Message.error("shop.register.disabledExist", new Object[0]);
		if ((!localSetting.getIsDuplicateEmail().booleanValue())
				&& (this.memberService.emailExists(email)))
			return Message.error("shop.register.emailExist", new Object[0]);
		Member localMember = new Member();
		List<MemberAttribute> localList = this.memberAttributeService
				.findList();
		Iterator<MemberAttribute> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			MemberAttribute memberAttribute = (MemberAttribute) localIterator
					.next();
			String memberAttributeId = request.getParameter("memberAttribute_"
					+ ((MemberAttribute) memberAttribute).getId());
			if ((((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.name)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.address)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.zipCode)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.phone)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.mobile)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.text)
					|| (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.select)) {
				if ((((MemberAttribute) memberAttribute).getIsRequired()
						.booleanValue())
						&& (StringUtils.isEmpty((String) memberAttributeId)))
					return Message.error("shop.common.invalid", new Object[0]);
				localMember.setAttributeValue(
						(MemberAttribute) memberAttribute, memberAttributeId);
			} else {
				Member.Gender localGender;
				if (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.gender) {
					localGender = StringUtils
							.isNotEmpty((String) memberAttributeId) ? Member.Gender
							.valueOf((String) memberAttributeId) : null;
					if ((((MemberAttribute) memberAttribute).getIsRequired()
							.booleanValue()) && (localGender == null))
						return Message.error("shop.common.invalid",
								new Object[0]);
					localMember.setGender(localGender);
				} else if (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.birth) {
					try {
						Date birthDay = StringUtils
								.isNotEmpty((String) memberAttributeId) ? DateUtils
								.parseDate((String) memberAttributeId,
										CommonAttributes.DATE_PATTERNS) : null;
						if ((((MemberAttribute) memberAttribute)
								.getIsRequired().booleanValue())
								&& (birthDay == null))
							return Message.error("shop.common.invalid",
									new Object[0]);
						localMember.setBirth(birthDay);
					} catch (ParseException localParseException1) {
						return Message.error("shop.common.invalid",
								new Object[0]);
					}
				} else if (((MemberAttribute) memberAttribute).getType() == MemberAttribute.Type.area) {
					Area area = StringUtils
							.isNotEmpty((String) memberAttributeId) ? (Area) this.areaService
							.find(Long.valueOf((String) memberAttributeId))
							: null;
					if (area != null)
						localMember.setArea((Area) area);
					else if (((MemberAttribute) memberAttribute)
							.getIsRequired().booleanValue())
						return Message.error("shop.common.invalid",
								new Object[0]);
				} else {
					if (((MemberAttribute) memberAttribute).getType() != MemberAttribute.Type.checkbox)
						continue;
					String[] memberAttributeIds = request
							.getParameterValues("memberAttribute_"
									+ ((MemberAttribute) memberAttribute)
											.getId());
					List<String> ids = memberAttributeIds != null ? Arrays
							.asList(memberAttributeIds) : null;
					if ((((MemberAttribute) memberAttribute).getIsRequired()
							.booleanValue())
							&& ((ids == null) || ((ids).isEmpty())))
						return Message.error("shop.common.invalid",
								new Object[0]);
					localMember.setAttributeValue(
							(MemberAttribute) memberAttribute, ids);
				}
			}
		}
		localMember.setUsername(username.toLowerCase());
		localMember.setPassword(DigestUtils.md5Hex(str));
		localMember.setEmail(email);
		localMember.setPoint(localSetting.getRegisterPoint());
		localMember.setAmount(new BigDecimal(0));
		localMember.setBalance(new BigDecimal(0));
		localMember.setIsEnabled(Boolean.valueOf(true));
		localMember.setIsLocked(Boolean.valueOf(false));
		localMember.setLoginFailureCount(Integer.valueOf(0));
		localMember.setLockedDate(null);
		localMember.setRegisterIp(request.getRemoteAddr());
		localMember.setLoginIp(request.getRemoteAddr());
		localMember.setLoginDate(new Date());
		localMember.setSafeKey(null);
		localMember.setMemberRank(this.memberRankService.findDefault());
		localMember.setFavoriteProducts(null);
		this.memberService.save(localMember);
		Object memberAttribute = this.cartService.getCurrent();
		if ((memberAttribute != null)
				&& (((Cart) memberAttribute).getMember() == null)) {
			this.cartService.merge(localMember, (Cart) memberAttribute);
			CookieUtils.removeCookie(request, response, "cartId");
			CookieUtils.removeCookie(request, response, "cartKey");
		}
		Map<String, Object> localMap = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		Enumeration enumer = session.getAttributeNames();
		while (enumer.hasMoreElements()) {
			String name = (String) enumer.nextElement();
			localMap.put(name, session.getAttribute(name));
		}
		session.invalidate();
		session = request.getSession();
		Iterator<Entry<String, Object>> iterator = localMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> pairs = (Entry<String, Object>) iterator
					.next();
			session.setAttribute((String) pairs.getKey(), pairs.getValue());
		}
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(
				localMember.getId(), localMember.getUsername()));
		CookieUtils.setCookie(request, response, "username",
				localMember.getUsername());
		return  Message.success("shop.register.success", new Object[0]);
	}
}