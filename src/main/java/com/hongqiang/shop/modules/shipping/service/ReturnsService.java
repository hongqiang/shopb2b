package com.hongqiang.shop.modules.shipping.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Returns;

public interface ReturnsService{

	public Returns find(Long id);
	
	public Page<Returns> findPage(Pageable pageable);
	  
	public void save(Returns returns);

  public Returns update(Returns returns);

  public Returns update(Returns returns, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Returns returns);
}