package com.hongqiang.shop.modules.account.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Payment;

public interface PaymentService {

	public Payment findBySn(String sn);

	public Payment find(Long id);

	public Page<Payment> findPage(Pageable pageable);

	public void save(Payment payment);

	public Payment update(Payment payment);

	public Payment update(Payment payment, String[] ignoreProperties);

	public void delete(Long id);

	public void delete(Long[] ids);

	public void delete(Payment payment);
}