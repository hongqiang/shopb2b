package com.hongqiang.shop.modules.shipping.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Filter;
import com.hongqiang.shop.common.utils.Order;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.DeliveryCenter;

public interface DeliveryCenterDao extends DeliveryCenterDaoCustom,
		CrudRepository<DeliveryCenter, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface DeliveryCenterDaoCustom extends BaseDao<DeliveryCenter,Long> {

	public DeliveryCenter findDefault();

	public List<DeliveryCenter> findAll();

	public List<DeliveryCenter> findList(Integer first, Integer count,
			List<Filter> filters, List<Order> orders);

	public Page<DeliveryCenter> findPage(Pageable pageable);

	public void persist(DeliveryCenter deliveryCenter);

	public DeliveryCenter merge(DeliveryCenter deliveryCenter);

}