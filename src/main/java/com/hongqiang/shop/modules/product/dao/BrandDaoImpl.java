package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Brand;

import org.springframework.stereotype.Repository;

@Repository
class BrandDaoImpl extends BaseDaoImpl<Brand,Long>  implements BrandDaoCustom{
  
	@Override
	public Page<Brand>  findPage(Pageable pageable){
		String qlString = "select brand from Brand brand where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable);
	}
	
	@Override
	public  List<Brand> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select brand from Brand brand where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
	
	@Override
	public List<Brand> findAll(){
		return findList(null, null, null, null);
	}
}