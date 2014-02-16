package com.hongqiang.shop.modules.user.service;

import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.Member;

public abstract interface DepositService {

	public Page<Deposit> findPage(Member paramMember, Pageable paramPageable);

	public Page<Deposit> findPage(Pageable pageable);
}