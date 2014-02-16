package com.hongqiang.shop.modules.account.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.account.dao.PaymentMethodDao;
import com.hongqiang.shop.modules.entity.PaymentMethod;

@Service
public class PaymentMethodServiceImpl extends BaseService implements
		PaymentMethodService {

	@Autowired
	private PaymentMethodDao paymentMethodDao;

	@Transactional
	public PaymentMethod find(Long id) {
		return this.paymentMethodDao.find(id);
	}

	@Transactional
	public Page<PaymentMethod> findPage(Pageable pageable) {
		return this.paymentMethodDao.findPage(pageable);
	}

	@Transactional
	public List<PaymentMethod> findAll(){
		return this.paymentMethodDao.findAll();
	}
	
	@Transactional
	public long count() {
		return this.paymentMethodDao.count();
	}

	@Transactional
	public void save(PaymentMethod paymentMethod) {
		this.paymentMethodDao.persist(paymentMethod);
	}

	@Transactional
	public PaymentMethod update(PaymentMethod paymentMethod) {
		return (PaymentMethod) this.paymentMethodDao.merge(paymentMethod);
	}

	@Transactional
	public PaymentMethod update(PaymentMethod paymentMethod,
			String[] ignoreProperties) {
		return (PaymentMethod) this.paymentMethodDao.update(paymentMethod,
				ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.paymentMethodDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.paymentMethodDao.delete(localSerializable);
	}

	@Transactional
	public void delete(PaymentMethod paymentMethod) {
		this.paymentMethodDao.delete(paymentMethod);
	}
}
