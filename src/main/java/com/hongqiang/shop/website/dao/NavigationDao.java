package com.hongqiang.shop.website.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.website.entity.Navigation;

public interface NavigationDao extends NavigationDaoCustom, CrudRepository<Navigation, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface NavigationDaoCustom extends BaseDao<Navigation,Long> {
  public List<Navigation> findList(Navigation.Position paramPosition);
  
  public  List<Navigation> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
}
