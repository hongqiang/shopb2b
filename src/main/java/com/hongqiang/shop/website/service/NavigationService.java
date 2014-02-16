package com.hongqiang.shop.website.service;

import java.util.List;

import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.website.entity.Navigation;

public interface NavigationService {
	public List<Navigation> findList(Navigation.Position paramPosition);

	public List<Navigation> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1, String paramString);
	
	public List<Navigation> findList(Integer paramInteger,
			List<Filter> paramList, List<Order> paramList1);

	public Navigation find(Long id);

	public void save(Navigation navigation);

	public Navigation update(Navigation navigation);

	public Navigation update(Navigation navigation, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Navigation navigation);
}