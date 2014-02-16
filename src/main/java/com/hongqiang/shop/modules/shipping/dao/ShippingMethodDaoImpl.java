package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ShippingMethod;

@Repository
public class ShippingMethodDaoImpl extends BaseDaoImpl<ShippingMethod,Long>
		implements ShippingMethodDaoCustom {

	@Override
	public Page<ShippingMethod> findPage(Pageable pageable) {
		String qlString = "select shippingMethod from ShippingMethod shippingMethod where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public  List<ShippingMethod> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select shippingMethod from ShippingMethod shippingMethod where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
	
	@Override
	public List<ShippingMethod> findAll(){
		return findList(null, null, null, null);
	}
	
	// @Override
	// public Long count(){
	// String qlString =
	// "select shippingMethod from ShippingMethod shippingMethod where 1=1 ";
	// List<Object> parameter = new ArrayList<Object>();
	// StringBuilder sBuilder = new StringBuilder(sqlString);
	// return super.count(sBuilder, null, parameter);
	// }
}