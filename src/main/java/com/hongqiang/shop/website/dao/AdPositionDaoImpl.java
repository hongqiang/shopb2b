package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.AdPosition;

@Repository
public class AdPositionDaoImpl extends BaseDaoImpl<AdPosition,Long>
  implements AdPositionDaoCustom{
  
	@Override
	public Page<AdPosition>  findPage(Pageable pageable){
		String qlString = "select adPosition from AdPosition adPosition where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage( qlString,  parameter, pageable) ;
	}
	
	@Override
	public  List<AdPosition> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select adPosition from AdPosition adPosition where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
	
	@Override
	public List<AdPosition> findAll(){
		return findList(null, null, null, null);
	}
}