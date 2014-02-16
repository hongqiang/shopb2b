package com.hongqiang.shop.modules.shipping.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.ShippingMethod;

public interface ShippingMethodDao extends ShippingMethodDaoCustom, CrudRepository<ShippingMethod, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ShippingMethodDaoCustom extends BaseDao<ShippingMethod,Long> {

	public Page<ShippingMethod>  findPage(Pageable pageable);
	
    public  List<ShippingMethod> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
	
	public List<ShippingMethod> findAll();
	

	
	//调用CrudRepository自带的返回数目的函数。
//	public Long count();
}
