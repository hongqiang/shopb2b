package com.hongqiang.shop.modules.user.web.shop;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.SafeKey;
import com.hongqiang.shop.modules.user.service.MemberService;
import com.hongqiang.shop.modules.util.service.CaptchaService;
import com.hongqiang.shop.website.service.MailService;

@Controller("shopPasswordController")
@RequestMapping({ "${frontPath}/password" })
public class PasswordController extends BaseController {

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MailService mailService;

	@RequestMapping(value = { "/find" }, method = RequestMethod.GET)
	public String find(Model model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/password/find";
	}

	@RequestMapping(value = { "/find" }, method = RequestMethod.POST)
	@ResponseBody
	public Message find(String captchaId, String captcha, String username,
			String email) {
		if (!this.captchaService.isValid(Setting.CaptchaType.findPassword,
				captchaId, captcha))
			return Message.error("shop.captcha.invalid", new Object[0]);
		if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(email)))
			return Message.error("shop.common.invalid", new Object[0]);
		Member localMember = this.memberService.findByUsername(username);
		if (localMember == null)
			return Message.error("shop.password.memberNotExist", new Object[0]);
		if (!localMember.getEmail().equalsIgnoreCase(email))
			return Message.error("shop.password.invalidEmail", new Object[0]);
		Setting localSetting = SettingUtils.get();
		SafeKey localSafeKey = new SafeKey();
		localSafeKey.setValue(UUID.randomUUID().toString()
				+ DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
		localSafeKey
				.setExpire(localSetting.getSafeKeyExpiryTime().intValue() != 0 ? DateUtils
						.addMinutes(new Date(), localSetting
								.getSafeKeyExpiryTime().intValue()) : null);
		localMember.setSafeKey(localSafeKey);
		this.memberService.update(localMember);
		this.mailService.sendFindPasswordMail(localMember.getEmail(),
				localMember.getUsername(), localSafeKey);
		return Message.success("shop.password.mailSuccess", new Object[0]);
	}

	@RequestMapping(value = { "/reset" }, method = RequestMethod.GET)
	public String reset(String username, String key, Model model) {
		Member localMember = this.memberService.findByUsername(username);
		if (localMember == null)
			return SHOP_ERROR_PAGE;
		SafeKey localSafeKey = localMember.getSafeKey();
		if ((localSafeKey == null) || (localSafeKey.getValue() == null)
				|| (!localSafeKey.getValue().equals(key)))
			return SHOP_ERROR_PAGE;
		if (localSafeKey.hasExpired()) {
			model.addAttribute("erroInfo",
					Message.warn("shop.password.hasExpired", new Object[0]));
			return SHOP_ERROR_PAGE;
		}
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		model.addAttribute("member", localMember);
		model.addAttribute("key", key);
		return "/shop/password/reset";
	}

	@RequestMapping(value = { "reset" }, method = RequestMethod.POST)
	@ResponseBody
	public Message reset(String captchaId, String captcha, String username,
			String newPassword, String key) {
		if (!this.captchaService.isValid(Setting.CaptchaType.resetPassword,
				captchaId, captcha))
			return Message.error("shop.captcha.invalid", new Object[0]);
		Member localMember = this.memberService.findByUsername(username);
		if (localMember == null)
			return SHOP_ERROR;
		if (!beanValidator(Member.class, "password", newPassword,
				new Class[] { BaseEntity.Save.class }))
			return Message.warn("shop.password.invalidPassword", new Object[0]);
		Setting localSetting = SettingUtils.get();
		if ((newPassword.length() < localSetting.getPasswordMinLength()
				.intValue())
				|| (newPassword.length() > localSetting.getPasswordMaxLength()
						.intValue()))
			return Message.warn("shop.password.invalidPassword", new Object[0]);
		SafeKey localSafeKey = localMember.getSafeKey();
		if ((localSafeKey == null) || (localSafeKey.getValue() == null)
				|| (!localSafeKey.getValue().equals(key)))
			return SHOP_ERROR;
		if (localSafeKey.hasExpired())
			return Message.error("shop.password.hasExpired", new Object[0]);
		localMember.setPassword(DigestUtils.md5Hex(newPassword));
		localSafeKey.setExpire(new Date());
		localSafeKey.setValue(null);
		this.memberService.update(localMember);
		return Message.success("shop.password.resetSuccess", new Object[0]);
	}
}