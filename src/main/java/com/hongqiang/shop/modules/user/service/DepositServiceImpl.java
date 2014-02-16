package com.hongqiang.shop.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.service.BaseService;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.Member;
import com.hongqiang.shop.modules.user.dao.DepositDao;

@Service
public class DepositServiceImpl extends BaseService implements DepositService {

	@Autowired
	private DepositDao depositDao;

	@Transactional(readOnly = true)
	public Page<Deposit> findPage(Member member, Pageable pageable) {
		return this.depositDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Deposit> findPage(Pageable pageable) {
		return this.depositDao.findPage(pageable);
	}
}