package com.hongqiang.shop.modules.product.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Tag;

@Repository
 class TagDaoImpl extends BaseDaoImpl<Tag, Long>
  implements TagDaoCustom
{

	@Override
	public Page<Tag>  findPage(Pageable pageable){
		String qlString = "select tag from Tag tag where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable) ;
	}
	
	@Override
  public List<Tag> findList(Tag.Type type)
  {
	String sqlString="select tag from Tag tag where tag.type =?  order by tag.order ASC";
	List<Object> parameter = new ArrayList<Object>();
	parameter.add(type);
	return super.findList(sqlString, parameter, null, null, null, null);
  }
	
	@Override
	public  List<Tag> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select tag from Tag tag where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
}