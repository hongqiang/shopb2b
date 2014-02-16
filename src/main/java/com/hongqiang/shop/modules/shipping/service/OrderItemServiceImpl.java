package com.hongqiang.shop.modules.shipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.modules.entity.OrderItem;
import com.hongqiang.shop.modules.shipping.dao.OrderItemDao;

@Service
public class OrderItemServiceImpl extends BaseService
  implements OrderItemService
{
    @Autowired
  private OrderItemDao orderItemDao;

   @Transactional
	public OrderItem find(Long id) {
		return this.orderItemDao.find(id);
	}
   
   @Transactional
  public void save(OrderItem orderItem)
  {
	this.orderItemDao.persist(orderItem);
  }

  @Transactional
  public OrderItem update(OrderItem orderItem)
  {
    return (OrderItem)this.orderItemDao.merge(orderItem);
  }

  @Transactional
  public OrderItem update(OrderItem orderItem, String[] ignoreProperties)
  {
    return (OrderItem)this.orderItemDao.update(orderItem, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.orderItemDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.orderItemDao.delete(localSerializable);
  }

  @Transactional
  public void delete(OrderItem orderItem)
  {
    this.orderItemDao.delete(orderItem);
  }
}