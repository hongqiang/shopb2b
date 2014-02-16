package com.hongqiang.shop.modules.shipping.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Refunds;

public interface RefundsService{

	public Refunds find(Long id);
	
	public Page<Refunds> findPage(Pageable pageable);
	  
	public void save(Refunds refunds);

  public Refunds update(Refunds refunds);

  public Refunds update(Refunds refunds, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Refunds refunds);
}