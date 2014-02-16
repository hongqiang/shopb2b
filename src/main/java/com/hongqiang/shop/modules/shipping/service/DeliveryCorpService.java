package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.DeliveryCorp;

public interface DeliveryCorpService{

	public DeliveryCorp find(Long id);
	
	public List<DeliveryCorp> findAll();
	
	public Page<DeliveryCorp> findPage(Pageable pageable);
	
	public void save(DeliveryCorp deliveryCorp);

  public DeliveryCorp update(DeliveryCorp deliveryCorp);

  public DeliveryCorp update(DeliveryCorp deliveryCorp, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(DeliveryCorp deliveryCorp);
}