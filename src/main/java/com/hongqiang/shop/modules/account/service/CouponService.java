package com.hongqiang.shop.modules.account.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;

public interface CouponService {
	public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable);

	public Coupon find(Long id);

	public Page<Coupon> findPage(Pageable pageable);

	public List<Coupon> findList(Long[] ids);
	
	public List<Coupon> findList(Integer count, List<Filter> filters,List<Order> orders);

	public List<Coupon> findAll();

	public void save(Coupon coupon);

	public Coupon update(Coupon coupon);

	public Coupon update(Coupon coupon, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Coupon coupon);
}