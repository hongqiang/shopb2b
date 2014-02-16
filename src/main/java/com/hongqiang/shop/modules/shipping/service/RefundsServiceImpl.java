package com.hongqiang.shop.modules.shipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Refunds;
import com.hongqiang.shop.modules.shipping.dao.RefundsDao;

@Service
public class RefundsServiceImpl extends BaseService
  implements RefundsService
{  
  @Autowired
  private RefundsDao refundsDao;

   @Transactional(readOnly=true)
	public Refunds find(Long id) {
		return this.refundsDao.find(id);
	}
   
 @Transactional(readOnly=true)
   public Page<Refunds> findPage(Pageable pageable){
	   return this.refundsDao.findPage(pageable);
   }
   
   @Transactional
  public void save(Refunds refunds)
  {
	this.refundsDao.persist(refunds);
  }

  @Transactional
  public Refunds update(Refunds refunds)
  {
    return (Refunds)this.refundsDao.merge(refunds);
  }

  @Transactional
  public Refunds update(Refunds refunds, String[] ignoreProperties)
  {
    return (Refunds)this.refundsDao.update(refunds, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.refundsDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.refundsDao.delete(localSerializable);
  }

  @Transactional
  public void delete(Refunds refunds)
  {
    this.refundsDao.delete(refunds);
  }
}