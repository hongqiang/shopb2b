package com.hongqiang.shop.modules.shipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Returns;
import com.hongqiang.shop.modules.shipping.dao.ReturnsDao;

@Service
public class ReturnsServiceImpl extends BaseService
  implements ReturnsService{

  @Autowired
  private ReturnsDao returnsDao;

   @Transactional
	public Returns find(Long id) {
		return this.returnsDao.find(id);
	}
   
 @Transactional
   public Page<Returns> findPage(Pageable pageable){
	   return this.returnsDao.findPage(pageable);
   }
   
   @Transactional
  public void save(Returns returns)
  {
	this.returnsDao.persist(returns);
  }

  @Transactional
  public Returns update(Returns returns)
  {
    return (Returns)this.returnsDao.merge(returns);
  }

  @Transactional
  public Returns update(Returns returns, String[] ignoreProperties)
  {
    return (Returns)this.returnsDao.update(returns, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.returnsDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.returnsDao.delete(localSerializable);
  }

  @Transactional
  public void delete(Returns returns)
  {
    this.returnsDao.delete(returns);
  }
}