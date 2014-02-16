package com.hongqiang.shop.modules.account.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;
import com.hongqiang.shop.modules.entity.CouponCode;
import com.hongqiang.shop.modules.entity.Member;

public interface CouponCodeDao extends CouponCodeDaoCustom, CrudRepository<CouponCode, Long> {

	@Query("from CouponCode couponCode where lower(couponCode.code) = lower(?1)")
	public CouponCode findByCode(String code);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface CouponCodeDaoCustom extends BaseDao<CouponCode,Long> {
  public boolean codeExists(String paramString);

  public CouponCode build(Coupon paramCoupon, Member paramMember);

  public List<CouponCode> build(Coupon paramCoupon, Member paramMember, Integer paramInteger);

  public Page<CouponCode> findPage(Member paramMember, Pageable paramPageable);
  
  public Page<CouponCode> findPage(Pageable paramPageable);

  public Long count(Coupon coupon, Member member, Boolean hasBegun, Boolean hasExpired, Boolean isUsed);
}