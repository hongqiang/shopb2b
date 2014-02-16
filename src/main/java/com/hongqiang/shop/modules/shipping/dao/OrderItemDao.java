package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.OrderItem;

public abstract interface OrderItemDao extends OrderItemDaoCustom, CrudRepository<OrderItem, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface OrderItemDaoCustom extends BaseDao<OrderItem,Long> {
}