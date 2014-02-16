package com.hongqiang.shop.modules.shipping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.DeliveryCenter;
import com.hongqiang.shop.modules.shipping.dao.DeliveryCenterDao;

@Service
public class DeliveryCenterServiceImpl extends BaseService implements
		DeliveryCenterService {

	@Autowired
	private DeliveryCenterDao deliveryCenterDao;

	@Transactional(readOnly = true)
	public DeliveryCenter findDefault() {
		return this.deliveryCenterDao.findDefault();
	}

	@Transactional(readOnly = true)
	public DeliveryCenter find(Long id) {
		return this.deliveryCenterDao.find(id);
	}

	@Transactional(readOnly = true)
	public List<DeliveryCenter> findAll(){
		return this.deliveryCenterDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<DeliveryCenter> findPage(Pageable pageable) {
		return this.deliveryCenterDao.findPage(pageable);
	}

	@Transactional
	public void save(DeliveryCenter deliveryCenter) {
		this.deliveryCenterDao.persist(deliveryCenter);
	}

	@Transactional
	public DeliveryCenter update(DeliveryCenter deliveryCenter) {
		return (DeliveryCenter) this.deliveryCenterDao.merge(deliveryCenter);
	}

	@Transactional
	public DeliveryCenter update(DeliveryCenter deliveryCenter,
			String[] ignoreProperties) {
		return (DeliveryCenter) this.deliveryCenterDao.update(deliveryCenter,
				ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.deliveryCenterDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.deliveryCenterDao.delete(localSerializable);
	}

	@Transactional
	public void delete(DeliveryCenter deliveryCenter) {
		this.deliveryCenterDao.delete(deliveryCenter);
	}
}