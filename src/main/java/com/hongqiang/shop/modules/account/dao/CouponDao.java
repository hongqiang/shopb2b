package com.hongqiang.shop.modules.account.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Coupon;

public interface CouponDao extends CouponDaoCustom,
		CrudRepository<Coupon, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface CouponDaoCustom extends BaseDao<Coupon,Long> {

	public Page<Coupon> findPage(Boolean isEnabled, Boolean isExchange,
			Boolean hasExpired, Pageable pageable);

	public Page<Coupon> findPage(Pageable pageable);
	
	public  List<Coupon> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders);
	
	public List<Coupon> findAll();
}