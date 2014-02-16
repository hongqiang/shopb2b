package com.hongqiang.shop.modules.shipping.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Receiver;

public interface ReceiverService{

  public Receiver findDefault(Member paramMember);

  public Page<Receiver> findPage(Member paramMember, Pageable paramPageable);
  
  public Receiver find(Long id);
  
  public void save(Receiver receiver);

  public Receiver update(Receiver receiver);

  public Receiver update(Receiver receiver, String[] ignoreProperties);
  
  public void delete(Long id);

  public void delete(Long[] ids);

  public void delete(Receiver receiver);
}