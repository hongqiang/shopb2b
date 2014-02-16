package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ShippingMethod;

public interface ShippingMethodService {

	public ShippingMethod find(Long id);

	public Page<ShippingMethod> findPage(Pageable pageable);

	public List<ShippingMethod> findList(Long[] ids);

	public List<ShippingMethod> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public List<ShippingMethod> findAll();

	public Long count();

	public void save(ShippingMethod shippingMethod);

	public ShippingMethod update(ShippingMethod shippingMethod);

	public ShippingMethod update(ShippingMethod shippingMethod,
			String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(ShippingMethod shippingMethod);
}