package com.hongqiang.shop.modules.shipping.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ShippingMethod;
import com.hongqiang.shop.modules.shipping.dao.ShippingMethodDao;

@Service
public class ShippingMethodServiceImpl extends BaseService
  implements ShippingMethodService
{
  @Autowired
  private ShippingMethodDao shippingMethodDao;

   @Transactional(readOnly = true)
	public ShippingMethod find(Long id) {
		return this.shippingMethodDao.find(id);
	}
   
 @Transactional(readOnly = true)
   public Page<ShippingMethod> findPage(Pageable pageable){
	   return this.shippingMethodDao.findPage(pageable);
   }
   
 @Transactional(readOnly = true)
 public List<ShippingMethod> findList(Long[] ids){
	 List<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();
	 List<Long> localIds = Arrays.asList(ids);
	  if (ids != null){
			for (Long id : localIds){
				ShippingMethod shippingMethod =this.shippingMethodDao.find(id);
				shippingMethods.add(shippingMethod);
			}
	  }
	  return shippingMethods;		
 }

 @Transactional(readOnly = true)
	public List<ShippingMethod> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders){
	 return this.shippingMethodDao.findList(first, count, filters, orders);
 }

 @Transactional(readOnly = true)
	public List<ShippingMethod> findAll(){
	 return this.shippingMethodDao.findAll();
 }
 
    @Transactional
   public Long count(){
	return this.shippingMethodDao.count();
   }
   
   @Transactional
  public void save(ShippingMethod shippingMethod)
  {
	this.shippingMethodDao.persist(shippingMethod);
  }

  @Transactional
  public ShippingMethod update(ShippingMethod shippingMethod)
  {
    return (ShippingMethod)this.shippingMethodDao.merge(shippingMethod);
  }

  @Transactional
  public ShippingMethod update(ShippingMethod shippingMethod, String[] ignoreProperties)
  {
    return (ShippingMethod)this.shippingMethodDao.update(shippingMethod, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.shippingMethodDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.shippingMethodDao.delete(localSerializable);
  }

  @Transactional
  public void delete(ShippingMethod shippingMethod)
  {
    this.shippingMethodDao.delete(shippingMethod);
  }
}