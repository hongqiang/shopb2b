package com.hongqiang.shop.modules.user.web.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.utils.Setting;
import com.hongqiang.shop.common.utils.SettingUtils;
import com.hongqiang.shop.common.utils.model.CommonAttributes;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.entity.Area;
import com.hongqiang.shop.modules.entity.BaseEntity;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.MemberAttribute;
import com.hongqiang.shop.modules.entity.MemberRank;
import com.hongqiang.shop.modules.user.service.AdminService;
import com.hongqiang.shop.modules.user.service.AreaService;
import com.hongqiang.shop.modules.user.service.MemberAttributeService;
import com.hongqiang.shop.modules.user.service.MemberRankService;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("adminMemberController")
@RequestMapping({ "${adminPath}/member" })
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRankService memberRankService;

	@Autowired
	private MemberAttributeService memberAttributeService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private AdminService adminService;

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
	public boolean checkEmail(String previousEmail, String email) {
		if (StringUtils.isEmpty(email))
			return false;
		return this.memberService.emailUnique(previousEmail, email);
	}

	@RequestMapping(value = { "/view" }, method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberAttributes",
				this.memberAttributeService.findList());
		model.addAttribute("member", this.memberService.find(id));
		return "/admin/member/view";
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", this.memberRankService.findAll());
		model.addAttribute("memberAttributes",
				this.memberAttributeService.findList());
		return "/admin/member/add";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	public String save(Member member, Long memberRankId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank((MemberRank) this.memberRankService
				.find(memberRankId));
		if (!beanValidator(redirectAttributes, member,
				new Class[] { BaseEntity.Save.class }))
			return ERROR_PAGE;
		Setting localSetting = SettingUtils.get();
		if ((member.getUsername().length() < localSetting
				.getUsernameMinLength().intValue())
				|| (member.getUsername().length() > localSetting
						.getUsernameMaxLength().intValue()))
			return ERROR_PAGE;
		if ((member.getPassword().length() < localSetting
				.getPasswordMinLength().intValue())
				|| (member.getPassword().length() > localSetting
						.getPasswordMaxLength().intValue()))
			return ERROR_PAGE;
		if ((this.memberService.usernameDisabled(member.getUsername()))
				|| (this.memberService.usernameExists(member.getUsername())))
			return ERROR_PAGE;
		if ((!localSetting.getIsDuplicateEmail().booleanValue())
				&& (this.memberService.emailExists(member.getEmail())))
			return ERROR_PAGE;
		member.removeAttributeValue();
		Iterator<MemberAttribute> localIterator = this.memberAttributeService
				.findList().iterator();
		while (localIterator.hasNext()) {
			MemberAttribute localMemberAttribute = (MemberAttribute) localIterator
					.next();
			String str = request.getParameter("memberAttribute_"
					+ localMemberAttribute.getId());
			if ((localMemberAttribute.getType() == MemberAttribute.Type.name)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.address)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.zipCode)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.phone)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.mobile)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.text)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.select)) {
				if ((localMemberAttribute.getIsRequired().booleanValue())
						&& (StringUtils.isEmpty(str)))
					return ERROR_PAGE;
				member.setAttributeValue(localMemberAttribute, str);
			} else {
				Member.Gender localGender;
				if (localMemberAttribute.getType() == MemberAttribute.Type.gender) {
					localGender = StringUtils.isNotEmpty(str) ? Member.Gender
							.valueOf(str) : null;
					if ((localMemberAttribute.getIsRequired().booleanValue())
							&& (localGender == null))
						return ERROR_PAGE;
					member.setGender(localGender);
				} else if (localMemberAttribute.getType() == MemberAttribute.Type.birth) {
					try {
						Date localDate = StringUtils.isNotEmpty(str) ? DateUtils
								.parseDate(str, CommonAttributes.DATE_PATTERNS)
								: null;
						if ((localMemberAttribute.getIsRequired()
								.booleanValue()) && (localDate == null))
							return ERROR_PAGE;
						member.setBirth(localDate);
					} catch (ParseException localParseException1) {
						return ERROR_PAGE;
					}
				} else {
					Object localObject1;
					if (localMemberAttribute.getType() == MemberAttribute.Type.area) {
						localObject1 = StringUtils.isNotEmpty(str) ? (Area) this.areaService
								.find(Long.valueOf(str)) : null;
						if (localObject1 != null)
							member.setArea((Area) localObject1);
						else if (localMemberAttribute.getIsRequired()
								.booleanValue())
							return ERROR_PAGE;
					} else {
						if (localMemberAttribute.getType() != MemberAttribute.Type.checkbox)
							continue;
						localObject1 = request
								.getParameterValues("memberAttribute_"
										+ localMemberAttribute.getId());
						List<Object> localObject2 = localObject1 != null ? Arrays
								.asList(localObject1) : null;
						if ((localMemberAttribute.getIsRequired()
								.booleanValue())
								&& ((localObject2 == null) || (localObject2
										.isEmpty())))
							return ERROR_PAGE;
						member.setAttributeValue(localMemberAttribute,
								localObject2);
					}
				}
			}
		}
		member.setUsername(member.getUsername().toLowerCase());
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setAmount(new BigDecimal(0));
		member.setIsLocked(Boolean.valueOf(false));
		member.setLoginFailureCount(Integer.valueOf(0));
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setSafeKey(null);
		member.setCart(null);
		member.setOrders(null);
		member.setDeposits(null);
		member.setPayments(null);
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteProducts(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		this.memberService.save(member, this.adminService.getCurrent());
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return (String) "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("memberRanks", this.memberRankService.findAll());
		model.addAttribute("memberAttributes",
				this.memberAttributeService.findList());
		model.addAttribute("member", this.memberService.find(id));
		return "/admin/member/edit";
	}

	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public String update(Member member, Long memberRankId, Integer modifyPoint,
			BigDecimal modifyBalance, String depositMemo,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank((MemberRank) this.memberRankService
				.find(memberRankId));
		if (!beanValidator(redirectAttributes, member, new Class[0]))
			return ERROR_PAGE;
		Setting localSetting = SettingUtils.get();
		if ((member.getPassword() != null) && (StringUtils.isNotEmpty(member.getPassword()))
				&& ((member.getPassword().length() < localSetting
						.getPasswordMinLength().intValue()) || (member
						.getPassword().length() > localSetting
						.getPasswordMaxLength().intValue())))
			return ERROR_PAGE;
		Member localMember = (Member) this.memberService.find(member.getId());
		if (localMember == null)
			return ERROR_PAGE;
		if ((!localSetting.getIsDuplicateEmail().booleanValue())
				&& (!this.memberService.emailUnique(localMember.getEmail(),
						member.getEmail())))
			return ERROR_PAGE;
		member.removeAttributeValue();
		Iterator<MemberAttribute> localIterator = this.memberAttributeService
				.findList().iterator();
		while (localIterator.hasNext()) {
			MemberAttribute localMemberAttribute = (MemberAttribute) localIterator
					.next();
			String str = request.getParameter("memberAttribute_"
					+ localMemberAttribute.getId());
			if ((localMemberAttribute.getType() == MemberAttribute.Type.name)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.address)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.zipCode)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.phone)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.mobile)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.text)
					|| (localMemberAttribute.getType() == MemberAttribute.Type.select)) {
				if ((localMemberAttribute.getIsRequired().booleanValue())
						&& (StringUtils.isEmpty(str)))
					return ERROR_PAGE;
				member.setAttributeValue(localMemberAttribute, str);
			} else {
				Member.Gender localGender;
				if (localMemberAttribute.getType() == MemberAttribute.Type.gender) {
					localGender = StringUtils.isNotEmpty(str) ? Member.Gender
							.valueOf(str) : null;
					if ((localMemberAttribute.getIsRequired().booleanValue())
							&& (localGender == null))
						return ERROR_PAGE;
					member.setGender(localGender);
				} else if (localMemberAttribute.getType() == MemberAttribute.Type.birth) {
					try {
						Date localDate = StringUtils.isNotEmpty(str) ? DateUtils
								.parseDate(str, CommonAttributes.DATE_PATTERNS)
								: null;
						if ((localMemberAttribute.getIsRequired()
								.booleanValue()) && (localDate == null))
							return ERROR_PAGE;
						member.setBirth(localDate);
					} catch (ParseException localParseException1) {
						return ERROR_PAGE;
					}
				} else {
					Object localObject1;
					if (localMemberAttribute.getType() == MemberAttribute.Type.area) {
						localObject1 = StringUtils.isNotEmpty(str) ? (Area) this.areaService
								.find(Long.valueOf(str)) : null;
						if (localObject1 != null)
							member.setArea((Area) localObject1);
						else if (localMemberAttribute.getIsRequired()
								.booleanValue())
							return ERROR_PAGE;
					} else {
						if (localMemberAttribute.getType() != MemberAttribute.Type.checkbox)
							continue;
						localObject1 = request
								.getParameterValues("memberAttribute_"
										+ localMemberAttribute.getId());
						List<Object> localObject2 = localObject1 != null ? Arrays
								.asList(localObject1) : null;
						if ((localMemberAttribute.getIsRequired()
								.booleanValue())
								&& ((localObject2 == null) || (localObject2
										.isEmpty())))
							return ERROR_PAGE;
						member.setAttributeValue(localMemberAttribute,
								localObject2);
					}
				}
			}
		}
		if (StringUtils.isEmpty(member.getPassword()))
			member.setPassword(localMember.getPassword());
		else
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		if ((localMember.getIsLocked().booleanValue())
				&& (!member.getIsLocked().booleanValue())) {
			member.setLoginFailureCount(Integer.valueOf(0));
			member.setLockedDate(null);
		} else {
			member.setIsLocked(localMember.getIsLocked());
			member.setLoginFailureCount(localMember.getLoginFailureCount());
			member.setLockedDate(localMember.getLockedDate());
		}
		BeanUtils.copyProperties(member, localMember, new String[] {
				"username", "point", "amount", "balance", "registerIp",
				"loginIp", "loginDate", "safeKey", "cart", "orders",
				"deposits", "payments", "couponCodes", "receivers", "reviews",
				"consultations", "favoriteProducts", "productNotifies",
				"inMessages", "outMessages" });
		this.memberService.update(localMember, modifyPoint, modifyBalance,
				depositMemo, this.adminService.getCurrent());
		addMessage(redirectAttributes, ADMIN_SUCCESS);
		return (String) "redirect:list.jhtml";
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("memberRanks", this.memberRankService.findAll());
		model.addAttribute("memberAttributes",
				this.memberAttributeService.findAll());
		model.addAttribute("page", this.memberService.findPage(pageable));
		return "/admin/member/list";
	}

	@RequestMapping(value = { "/delete" }, method = RequestMethod.POST)
	@ResponseBody
	public Message delete(Long[] ids) {
		if (ids != null) {
			for (Long localLong : ids) {
				Member localMember = (Member) this.memberService
						.find(localLong);
				if ((localMember != null)
						&& (localMember.getBalance().compareTo(
								new BigDecimal(0)) > 0))
					return Message.error(
							"admin.member.deleteExistDepositNotAllowed",
							new Object[] { localMember.getUsername() });
			}
			this.memberService.delete(ids);
		}
		return ADMIN_SUCCESS;
	}
}