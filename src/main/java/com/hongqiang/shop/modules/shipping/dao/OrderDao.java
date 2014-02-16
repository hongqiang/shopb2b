package com.hongqiang.shop.modules.shipping.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Member;

public interface OrderDao extends OrderDaoCustom, CrudRepository<com.hongqiang.shop.modules.entity.Order, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface OrderDaoCustom extends BaseDao<com.hongqiang.shop.modules.entity.Order,Long> {

	public com.hongqiang.shop.modules.entity.Order findBySn(String sn);
	
	public List<com.hongqiang.shop.modules.entity.Order> findList(Member member, Integer count, List<Filter> filters,List<Order> orders);

	public Page<com.hongqiang.shop.modules.entity.Order> findPage(Member member, Pageable pageable);

	public Page<com.hongqiang.shop.modules.entity.Order> findPage(
			com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired, Pageable pageable);

	public Long count(com.hongqiang.shop.modules.entity.Order.OrderStatus orderStatus,
			com.hongqiang.shop.modules.entity.Order.PaymentStatus paymentStatus,
			com.hongqiang.shop.modules.entity.Order.ShippingStatus shippingStatus,
			Boolean hasExpired);

	public Long waitingPaymentCount(Member member);

	public Long waitingShippingCount(Member member);

	public BigDecimal getSalesAmount(Date beginDate, Date endDate);

	public Integer getSalesVolume(Date beginDate, Date endDate);

	public void releaseStock();
}