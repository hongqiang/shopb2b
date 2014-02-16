package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Specification;

@Repository
public class SpecificationDaoImpl extends BaseDaoImpl<Specification, Long> implements
		SpecificationDaoCustom {
	@Override
	public Page<Specification> findPage(Pageable pageable) {
		String qlString = "select specification from Specification specification where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString, parameter, pageable);
	}

	@Override
	public List<Specification> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders) {
		String qlString = "select specification from Specification specification where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters,orders);
	}

	@Override
	public List<Specification> findAll() {
		return findList(null, null, null, null);
	}
}