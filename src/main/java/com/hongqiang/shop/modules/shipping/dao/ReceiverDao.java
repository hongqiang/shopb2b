package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Receiver;

public interface ReceiverDao extends ReceiverDaoCustom, CrudRepository<Receiver, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ReceiverDaoCustom extends BaseDao<Receiver,Long> {
  public Receiver findDefault(Member paramMember);

  public Page<Receiver> findPage(Member paramMember, Pageable paramPageable);
  
  public void persist(Receiver receiver);
  
  public Receiver merge(Receiver receiver);
}