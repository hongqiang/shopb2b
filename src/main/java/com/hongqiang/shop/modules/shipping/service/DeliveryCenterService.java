package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.DeliveryCenter;

public interface DeliveryCenterService{

  public DeliveryCenter findDefault();
  
  public DeliveryCenter find(Long id);
  
  public List<DeliveryCenter> findAll();
  
  public Page<DeliveryCenter> findPage(Pageable pageable);
  
  public void save(DeliveryCenter deliveryCenter);

  public DeliveryCenter update(DeliveryCenter deliveryCenter);

  public DeliveryCenter update(DeliveryCenter deliveryCenter, String[] ignoreProperties);

  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(DeliveryCenter deliveryCenter);
}