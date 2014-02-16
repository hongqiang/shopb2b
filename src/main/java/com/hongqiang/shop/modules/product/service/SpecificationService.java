package com.hongqiang.shop.modules.product.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Specification;

public abstract interface SpecificationService {

	public Specification find(Long id);

	public Page<Specification> findPage(Pageable pageable);

	public List<Specification> findList(Integer count, List<Filter> filters,
			List<Order> orders, String cacheRegion);

	public List<Specification> findAll();

	public void save(Specification specification);

	public Specification update(Specification specification);

	public Specification update(Specification brand, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Specification specification);
}