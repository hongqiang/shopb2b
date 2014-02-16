package com.hongqiang.shop.modules.account.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;

@Repository
public class CouponDaoImpl extends BaseDaoImpl<Coupon,Long> implements
		CouponDaoCustom {
	@Override
	public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable) {
		String sqlString = "select coupon from Coupon coupon where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (isEnabled != null) {
			sqlString += " and coupon.isEnabled = ? ";
			params.add(isEnabled);
		}
		if (isExchange != null) {
			sqlString += " and coupon.isExchange = ? ";
			params.add(isExchange);
		}
		Date nowadays = new Date();
		if (hasExpired != null) {
			if (hasExpired.booleanValue()) {
				sqlString += " and (coupon.endDate != null and coupon.endDate < ?) ";
				params.add(nowadays);
			} else {
				sqlString += " and (coupon.endDate != null or coupon.endDate >= ?) ";
				params.add(nowadays);
			}
		}
		return super.findPage(sqlString, params, pageable);
	}

	@Override
	public Page<Coupon> findPage(Pageable pageable) {
		String qlString = "select coupon from Coupon coupon where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public List<Coupon> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select coupon from Coupon coupon where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,
				orders);
	}

	@Override
	public List<Coupon> findAll(){
		return findList(null, null, null, null);
	}
}