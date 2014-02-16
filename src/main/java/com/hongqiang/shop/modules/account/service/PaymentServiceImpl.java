package com.hongqiang.shop.modules.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.account.dao.PaymentDao;
import com.hongqiang.shop.modules.entity.Payment;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

	@Autowired
	private PaymentDao paymentDao;

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return this.paymentDao.findBySn(sn);
	}

	@Transactional
	public Payment find(Long id) {
		return this.paymentDao.find(id);
	}

	@Transactional
	public Page<Payment> findPage(Pageable pageable) {
		return this.paymentDao.findPage(pageable);
	}

	@Transactional
	public void save(Payment payment) {
		this.paymentDao.persist(payment);
	}

	@Transactional
	public Payment update(Payment payment) {
		return (Payment) this.paymentDao.merge(payment);
	}

	@Transactional
	public Payment update(Payment payment, String[] ignoreProperties) {
		return (Payment) this.paymentDao.update(payment, ignoreProperties);
	}

	@Transactional
	public void delete(Long id) {

		this.paymentDao.delete(id);
	}

	@Transactional
	public void delete(Long[] ids) {
		if (ids != null)
			for (Long localSerializable : ids)
				this.paymentDao.delete(localSerializable);
	}

	@Transactional
	public void delete(Payment payment) {
		this.paymentDao.delete(payment);
	}
}