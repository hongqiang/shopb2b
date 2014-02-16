package com.hongqiang.shop.modules.shipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.entity.Receiver;
import com.hongqiang.shop.modules.shipping.dao.ReceiverDao;

@Service
public class ReceiverServiceImpl extends BaseService
  implements ReceiverService
{

  @Autowired
  private ReceiverDao receiverDao;

  @Transactional(readOnly=true)
  public Receiver findDefault(Member member)
  {
    return this.receiverDao.findDefault(member);
  }

  @Transactional(readOnly=true)
  public Page<Receiver> findPage(Member member, Pageable pageable)
  {
    return this.receiverDao.findPage(member, pageable);
  }
  
   @Transactional(readOnly=true)
	public Receiver find(Long id) {
		return this.receiverDao.find(id);
	}
	
	 @Transactional
  public void save(Receiver receiver)
  {
	this.receiverDao.persist(receiver);
  }

  @Transactional
  public Receiver update(Receiver receiver)
  {
    return (Receiver)this.receiverDao.merge(receiver);
  }

  @Transactional
  public Receiver update(Receiver receiver, String[] ignoreProperties)
  {
    return (Receiver)this.receiverDao.update(receiver, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.receiverDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.receiverDao.delete(localSerializable);
  }

  @Transactional
  public void delete(Receiver receiver)
  {
    this.receiverDao.delete(receiver);
  }
}