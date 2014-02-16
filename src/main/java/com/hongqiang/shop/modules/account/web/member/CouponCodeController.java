package com.hongqiang.shop.modules.account.web.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hongqiang.shop.common.utils.Message;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.common.web.BaseController;
import com.hongqiang.shop.modules.account.service.CouponCodeService;
import com.hongqiang.shop.modules.account.service.CouponService;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.service.MemberService;

@Controller("shopMemberCouponCodeController")
@RequestMapping({ "${memberPath}/coupon_code" })
public class CouponCodeController extends BaseController {
	private static final int PAGE_SIZE = 10;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponCodeService couponCodeService;

	@RequestMapping(value = { "/exchange" }, method = RequestMethod.GET)
	public String exchange(Integer pageNumber, ModelMap model) {
		Pageable localPageable = new Pageable(pageNumber,
				Integer.valueOf(PAGE_SIZE));
		model.addAttribute("page", this.couponService.findPage(
				Boolean.valueOf(true), Boolean.valueOf(true),
				Boolean.valueOf(false), localPageable));
		return "shop/member/coupon_code/exchange";
	}

	@RequestMapping(value = { "/exchange" }, method = RequestMethod.POST)
	@ResponseBody
	public Message exchange(Long id) {
		Coupon localCoupon = (Coupon) this.couponService.find(id);
		if ((localCoupon == null)
				|| (!localCoupon.getIsEnabled().booleanValue())
				|| (!localCoupon.getIsExchange().booleanValue())
				|| (localCoupon.hasExpired()))
			return SHOP_ERROR;
		Member localMember = this.memberService.getCurrent();
		if (localMember.getPoint().longValue() < localCoupon.getPoint()
				.intValue())
			return Message.warn("shop.member.couponCode.point", new Object[0]);
		this.couponCodeService.exchange(localCoupon, localMember);
		return SHOP_SUCCESS;
	}

	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String list(Integer pageNumber, ModelMap model) {
		Member localMember = this.memberService.getCurrent();
		Pageable localPageable = new Pageable(pageNumber,
				Integer.valueOf(PAGE_SIZE));
		model.addAttribute("page",
				this.couponCodeService.findPage(localMember, localPageable));
		return "shop/member/coupon_code/list";
	}
}