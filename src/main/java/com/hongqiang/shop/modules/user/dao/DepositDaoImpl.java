package com.hongqiang.shop.modules.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Deposit;
import com.hongqiang.shop.modules.entity.Member;

@Repository
public class DepositDaoImpl extends BaseDaoImpl<Deposit,Long>
  implements DepositDaoCustom
{
	@Override
  public Page<Deposit> findPage(Member member, Pageable pageable)
  {
    if (member == null){
    	List<Deposit> deposits = new ArrayList<Deposit>();
		return new Page<Deposit>(deposits,0L,pageable);
	}
	String sqlString = "select deposit from Deposit deposit where deposit.member = ? ";
	List<Object> params = new ArrayList<Object>();
	params.add(member);
	return super.findPage(sqlString,  params, pageable);
  }
  
  @Override
  public Page<Deposit> findPage(Pageable pageable){
		String sqlString = "select deposit from Deposit deposit where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		return super.findPage(sqlString,  params, pageable);
  }
}