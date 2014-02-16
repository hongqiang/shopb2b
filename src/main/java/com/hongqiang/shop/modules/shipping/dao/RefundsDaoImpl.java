package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Refunds;

@Repository
public class RefundsDaoImpl extends BaseDaoImpl<Refunds,Long>
  implements RefundsDaoCustom{
	
	@Override
	public Page<Refunds>  findPage(Pageable pageable){
		String qlString = "select refunds from Refunds refunds where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}
}