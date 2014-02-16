package com.hongqiang.shop.modules.account.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;

public interface CouponCodeService {
	public boolean codeExists(String paramString);

	public CouponCode findByCode(String paramString);

	public CouponCode find(Long id);

	public Page<CouponCode> findPage(Pageable pageable);

	public CouponCode build(Coupon paramCoupon, Member paramMember);

	public List<CouponCode> build(Coupon paramCoupon, Member paramMember,
			Integer paramInteger);

	public CouponCode exchange(Coupon paramCoupon, Member paramMember);

	public Page<CouponCode> findPage(Member paramMember, Pageable paramPageable);

	public Long count(Coupon coupon, Member member, Boolean hasBegun,
			Boolean hasExpired, Boolean isUsed);

	public void save(CouponCode couponCode);

	public CouponCode update(CouponCode couponCode);

	public CouponCode update(CouponCode couponCode, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(CouponCode couponCode);
}