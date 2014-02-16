package com.hongqiang.shop.modules.shipping.service;

import com.hongqiang.shop.modules.entity.OrderLog;

public interface OrderLogService{

	public OrderLog find(Long id);
	
	public void save(OrderLog orderLog);

  public OrderLog update(OrderLog orderLog);

  public OrderLog update(OrderLog orderLog, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(OrderLog orderLog);
}