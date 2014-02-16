package com.hongqiang.shop.modules.account.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;

@Repository
public class CouponCodeDaoImpl extends BaseDaoImpl<CouponCode,Long> implements
		CouponCodeDaoCustom {
	@Override
	public boolean codeExists(String code) {
		if (code == null)
			return false;
		String str = "select count(*) from CouponCode couponCode where lower(couponCode.code) = lower(:code)";
		Long localLong = (Long) this.getEntityManager()
				.createQuery(str, Long.class)
				.setFlushMode(FlushModeType.COMMIT).setParameter("code", code)
				.getSingleResult();
		return localLong.longValue() > 0L;
	}

	@Override
	public CouponCode build(Coupon coupon, Member member) {
		CouponCode couponCode = new CouponCode();
		String random = UUID.randomUUID().toString().toUpperCase();
		couponCode.setCode(coupon.getPrefix() + random.substring(0, 8)
				+ random.substring(9, 13) + random.substring(14, 18)
				+ random.substring(19, 23) + random.substring(24));
		couponCode.setIsUsed(Boolean.valueOf(false));
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		super.persist(couponCode);
		return couponCode;
	}

	@Override
	public List<CouponCode> build(Coupon coupon, Member member, Integer count) {
		List<CouponCode> couponCodes = new ArrayList<CouponCode>();
		for (int i = 0; i < count.intValue(); i++) {
			CouponCode localCouponCode = build(coupon, member);
			couponCodes.add(localCouponCode);
		}
		super.flush();
		super.clear();
		return couponCodes;
	}

	@Override
	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		String sqlString = " select couponCode from CouponCode couponCode where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (member != null) {
			sqlString += " and couponCode.member = ? ";
			params.add(member);
		}
		return super.findPage(sqlString, params, pageable);
	}

	@Override
	public Page<CouponCode> findPage(Pageable pageable) {
		String qlString = "select couponCode from CouponCode couponCode where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public Long count(Coupon coupon, Member member, Boolean hasBegun,
			Boolean hasExpired, Boolean isUsed) {
		String sqlString = " select couponCode from CouponCode couponCode where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (coupon != null) {
			sqlString += " and couponCode.coupon = ? ";
			params.add(coupon);
		}
		if (member != null) {
			sqlString += " and couponCode.member = ? ";
			params.add(member);
		}
		Date nowaday = new Date();
		if (hasBegun != null) {
			if (hasBegun.booleanValue()) {
				sqlString += " and (couponCode.coupon.beginDate != null or couponCode.coupon.beginDate <= ?) ";
				params.add(nowaday);
			} else {
				sqlString += " and (couponCode.coupon.beginDate != null and couponCode.coupon.beginDate > ?) ";
				params.add(nowaday);
			}
		}
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				sqlString += " and (couponCode.coupon.endDate != null and couponCode.coupon.beginDate < ?) ";
				params.add(nowaday);
			} else {
				sqlString += " and (couponCode.coupon.endDate != null or couponCode.coupon.beginDate >= ?) ";
				params.add(nowaday);
			}
		}
		if (isUsed != null) {
			sqlString += " and couponCode.isUsed = ? ";
			params.add(isUsed);
		}
		StringBuilder stringBuilder = new StringBuilder(sqlString);
		return super.count(stringBuilder, null, params);
	}
}