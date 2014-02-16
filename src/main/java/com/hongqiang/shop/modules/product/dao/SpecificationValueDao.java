package com.hongqiang.shop.modules.product.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.modules.entity.SpecificationValue;

public interface SpecificationValueDao extends SpecificationValueCustom,
		CrudRepository<SpecificationValue, Long> {
	public SpecificationValue findById(Long id);
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface SpecificationValueCustom extends BaseDao<SpecificationValue, Long> {
	public List<SpecificationValue> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<SpecificationValue> findAll();
}