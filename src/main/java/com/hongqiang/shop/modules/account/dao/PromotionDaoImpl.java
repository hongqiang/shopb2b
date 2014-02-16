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
import com.hongqiang.shop.modules.entity.Promotion;

@Repository
public class PromotionDaoImpl extends BaseDaoImpl<Promotion,Long> implements
		PromotionDaoCustom {
	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders) {
		String qlString = "select promotion from Promotion promotion where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		Date nowadays = new Date();
		if (hasBegun != null) {
			if (hasBegun.booleanValue()) {
				qlString += " and (promotion.beginDate is null or promotion.beginDate <= ?)";
				parameter.add(nowadays);
			} else {
				qlString += " and (promotion.beginDate is not null and promotion.beginDate > ?)";
				parameter.add(nowadays);
			}
		}
		if (hasEnded != null) {
			if (hasEnded.booleanValue()) {
				qlString += " and (promotion.endDate is not null and promotion.endDate < ?)";
				parameter.add(nowadays);
			} else {
				qlString += " and (promotion.endDate is null or promotion.endDate >= ?)";
				parameter.add(nowadays);
			}
		}
		return super
				.findList(qlString, parameter, null, count, filters, orders);
	}

	@Override
	public Page<Promotion> findPage(Pageable pageable) {
		String qlString = "select promotion from Promotion promotion where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public List<Promotion> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select promotion from Promotion promotion where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,
				orders);
	}
	
	@Override
	public List<Promotion> findAll(){
		return findList(null, null, null, null);
	}
}