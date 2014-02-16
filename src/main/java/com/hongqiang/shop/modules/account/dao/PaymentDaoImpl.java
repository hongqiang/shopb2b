package com.hongqiang.shop.modules.account.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Payment;

@Repository
public class PaymentDaoImpl extends BaseDaoImpl<Payment,Long>
  implements PaymentDaoCustom
{
	@Override
	public Page<Payment>  findPage(Pageable pageable){
		String qlString = "select payment from Payment payment where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}
}