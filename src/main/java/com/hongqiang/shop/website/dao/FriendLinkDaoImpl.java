package com.hongqiang.shop.website.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.website.entity.FriendLink;

@Repository
public class FriendLinkDaoImpl extends BaseDaoImpl<FriendLink,Long>
  implements FriendLinkDaoCustom
{
 @Override
  public List<FriendLink> findList(FriendLink.Type type)
  {
	String sqlString="select friendLink from FriendLink friendLink where friendLink.type =?  order by friendLink.order ASC";
	List<Object> parameter = new ArrayList<Object>();
	parameter.add(type);
	return super.findList(sqlString, parameter, null, null, null, null);
  }
  
 @Override
	public  List<FriendLink> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders){
		String qlString = "select friendLink from FriendLink friendLink where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findList(qlString, parameter, first, count, filters, orders);
	}
 
  @Override
	public Page<FriendLink>  findPage(Pageable pageable){
		String qlString = "select friendLink from FriendLink friendLink where 1=1 ";
		List<Object> parameter = new ArrayList<Object>();
		return super.findPage(qlString,  parameter, pageable) ;
	}
}