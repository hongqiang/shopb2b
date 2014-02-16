package com.hongqiang.shop.modules.account.service;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.account.dao.CouponCodeDao;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.dao.MemberDao;

@Service
public class CouponCodeServiceImpl extends BaseService implements
		CouponCodeService {

	@Autowired
	private CouponCodeDao couponCodeDao;

	@Autowired
	private MemberDao memberDao;

	@Transactional(readOnly = true)
	public boolean codeExists(String code) {
		return this.couponCodeDao.codeExists(code);
	}

	@Transactional(readOnly = true)
	public CouponCode findByCode(String code) {
		return this.couponCodeDao.findByCode(code);
	}

	@Transactional(readOnly = true)
	public CouponCode find(Long id) {
		return this.couponCodeDao.find(id);
	}

	@Transactional(readOnly = true)
	public Page<CouponCode> findPage(Pageable pageable) {
		return this.couponCodeDao.findPage(pageable);
	}

	public CouponCode build(Coupon coupon, Member member) {
		return this.couponCodeDao.build(coupon, member);
	}

	public List<CouponCode> build(Coupon coupon, Member member, Integer count) {
		return this.couponCodeDao.build(coupon, member, count);
	}

	public CouponCode exchange(Coupon coupon, Member member) {
		Assert.notNull(coupon);
		Assert.notNull(member);
		this.memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		member.setPoint(Long.valueOf(member.getPoint().longValue()
				- coupon.getPoint().intValue()));
		this.memberDao.merge(member);
		return this.couponCodeDao.build(coupon, member);
	}

	@Transactional(readOnly = true)
	public Page<CouponCode> findPage(Member member, Pageable pageable) {
		return this.couponCodeDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Coupon coupon, Member member, Boolean hasBegun,
			Boolean hasExpired, Boolean isUsed) {
		return this.couponCodeDao.count(coupon, member, hasBegun, hasExpired,
				isUsed);
	}

	@Transactional(readOnly = true)
	public void save(CouponCode couponCode) {
		this.couponCodeDao.persist(couponCode);
	}

	@Transactional(readOnly = true)
	public CouponCode update(CouponCode couponCode) {
		return (CouponCode) this.couponCodeDao.merge(couponCode);
	}

	@Transactional(readOnly = true)
	public CouponCode update(CouponCode couponCode, String[] ignoreProperties) {
		return (CouponCode) this.couponCodeDao.update(couponCode,
				ignoreProperties);
	}

	@Transactional(readOnly = true)
	public void delete(Long id) {
		this.couponCodeDao.delete(id);
	}

	@Transactional(readOnly = true)
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.couponCodeDao.delete(localSerializable);
	}

	@Transactional(readOnly = true)
	public void delete(CouponCode couponCode) {
		this.couponCodeDao.delete(couponCode);
	}
}