package com.hongqiang.shop.modules.shipping.service;

import com.hongqiang.shop.modules.entity.OrderItem;

public interface OrderItemService{

	public OrderItem find(Long id);
	
	public void save(OrderItem orderItem);

  public OrderItem update(OrderItem orderItem);

  public OrderItem update(OrderItem orderItem, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(OrderItem orderItem);
}