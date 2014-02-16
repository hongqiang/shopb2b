package com.hongqiang.shop.modules.shipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.modules.entity.OrderLog;
import com.hongqiang.shop.modules.shipping.dao.OrderLogDao;

@Service
public class OrderLogServiceImpl extends BaseService
  implements OrderLogService
{
   @Autowired
  private OrderLogDao orderLogDao;

   @Transactional
	public OrderLog find(Long id) {
		return this.orderLogDao.find(id);
	}
   
   @Transactional
  public void save(OrderLog orderLog)
  {
	this.orderLogDao.persist(orderLog);
  }

  @Transactional
  public OrderLog update(OrderLog orderLog)
  {
    return (OrderLog)this.orderLogDao.merge(orderLog);
  }

  @Transactional
  public OrderLog update(OrderLog orderLog, String[] ignoreProperties)
  {
    return (OrderLog)this.orderLogDao.update(orderLog, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.orderLogDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.orderLogDao.delete(localSerializable);
  }

  @Transactional
  public void delete(OrderLog orderLog)
  {
    this.orderLogDao.delete(orderLog);
  }
}