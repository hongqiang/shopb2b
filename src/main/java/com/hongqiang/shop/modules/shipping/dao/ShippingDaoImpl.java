package com.hongqiang.shop.modules.shipping.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Shipping;

@Repository
public class ShippingDaoImpl extends BaseDaoImpl<Shipping,Long>
  implements ShippingDaoCustom
{
		@Override
	public Page<Shipping>  findPage(Pageable pageable){
		String qlString = "select shipping from Shipping shipping where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}
}