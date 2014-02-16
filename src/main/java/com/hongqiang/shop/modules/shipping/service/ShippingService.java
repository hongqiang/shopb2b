package com.hongqiang.shop.modules.shipping.service;

import java.util.Map;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Shipping;

public interface ShippingService {

	public Shipping findBySn(String paramString);

	public Map<String, Object> query(Shipping paramShipping);

	public Shipping find(Long id);

	public Page<Shipping> findPage(Pageable pageable);

	public void save(Shipping paramShipping);

	public Shipping update(Shipping paramShipping);

	public Shipping update(Shipping paramShipping, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Shipping paramShipping);
}