package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.DeliveryCorp;
import com.hongqiang.shop.modules.shipping.dao.DeliveryCorpDao;

@Service
public class DeliveryCorpServiceImpl extends BaseService
  implements DeliveryCorpService
{
  @Autowired
  private DeliveryCorpDao deliveryCorpDao;

   @Transactional
	public DeliveryCorp find(Long id) {
		return this.deliveryCorpDao.find(id);
	}
   
   @Transactional
   public List<DeliveryCorp> findAll(){
	return this.deliveryCorpDao.findAll();
   }
   
 @Transactional
   public Page<DeliveryCorp> findPage(Pageable pageable){
	   return this.deliveryCorpDao.findPage(pageable);
   }
   
   @Transactional
  public void save(DeliveryCorp deliveryCorp)
  {
	this.deliveryCorpDao.persist(deliveryCorp);
  }

  @Transactional
  public DeliveryCorp update(DeliveryCorp deliveryCorp)
  {
    return (DeliveryCorp)this.deliveryCorpDao.merge(deliveryCorp);
  }

  @Transactional
  public DeliveryCorp update(DeliveryCorp deliveryCorp, String[] ignoreProperties)
  {
    return (DeliveryCorp)this.deliveryCorpDao.update(deliveryCorp, ignoreProperties);
  }

  @Transactional
  public void delete(Long id)
  {

    this.deliveryCorpDao.delete(id);
  }

  @Transactional
  public void delete(Long[] ids)
  {
	  if (ids != null)
			for (Long localSerializable : ids)
				this.deliveryCorpDao.delete(localSerializable);
  }

  @Transactional
  public void delete(DeliveryCorp deliveryCorp)
  {
    this.deliveryCorpDao.delete(deliveryCorp);
  }
}