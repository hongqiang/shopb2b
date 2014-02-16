package com.hongqiang.shop.modules.account.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Promotion;

public interface PromotionService {

	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders);

	public List<Promotion> findList(Boolean hasBegun, Boolean hasEnded,
			Integer count, List<Filter> filters, List<Order> orders,
			String cacheRegion);

	public List<Promotion> findAll();

	public Promotion find(Long id);

	public Page<Promotion> findPage(Pageable pageable);

	public List<Promotion> findList(Integer count, List<Filter> filters,
			List<Order> orders);

	public void save(Promotion promotion);

	public Promotion update(Promotion promotion);

	public Promotion update(Promotion promotion, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Promotion promotion);
}