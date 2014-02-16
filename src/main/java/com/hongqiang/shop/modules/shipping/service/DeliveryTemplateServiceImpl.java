package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.shipping.dao.DeliveryTemplateDao;
import com.hongqiang.shop.website.entity.DeliveryTemplate;

@Service
public class DeliveryTemplateServiceImpl extends BaseService
  implements DeliveryTemplateService
{

  @Autowired
  private DeliveryTemplateDao deliveryTemplateDao;
  
  @Transactional(readOnly=true)
  public DeliveryTemplate findDefault()
  {
    return this.deliveryTemplateDao.findDefault();
  }
  
  @Transactional(readOnly = true)
	public DeliveryTemplate find(Long id) {
		return this.deliveryTemplateDao.find(id);
	}
  
  @Transactional(readOnly = true)
  public List<DeliveryTemplate> findAll(){
	  return this.deliveryTemplateDao.findAll();
  }
  
	@Transactional(readOnly = true)
	public Page<DeliveryTemplate> findPage(Pageable pageable) {
		return this.deliveryTemplateDao.findPage(pageable);
	}

	@Transactional
	public void save(DeliveryTemplate deliveryTemplate) {
		this.deliveryTemplateDao.persist(deliveryTemplate);
	}

	@Transactional
	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate) {
		return (DeliveryTemplate) this.deliveryTemplateDao.merge(deliveryTemplate);
	}

	@Transactional
	public DeliveryTemplate update(DeliveryTemplate deliveryTemplate,
			String[] ignoreProperties) {
		return (DeliveryTemplate) this.deliveryTemplateDao.update(deliveryTemplate,
				ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.deliveryTemplateDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.deliveryTemplateDao.delete(localSerializable);
	}

	@Transactional
	public void delete(DeliveryTemplate deliveryTemplate) {
		this.deliveryTemplateDao.delete(deliveryTemplate);
	}
}