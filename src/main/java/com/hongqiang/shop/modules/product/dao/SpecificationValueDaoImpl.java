package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.modules.entity.SpecificationValue;

@Repository
public class SpecificationValueDaoImpl extends BaseDaoImpl<SpecificationValue, Long>
		implements SpecificationValueCustom {
	@Override
	public List<SpecificationValue> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select specificationValue from SpecificationValue specificationValue"+
			"where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,orders);
	}

	@Override
	public List<SpecificationValue> findAll() {
		return findList(null, null, null, null);
	}
}
