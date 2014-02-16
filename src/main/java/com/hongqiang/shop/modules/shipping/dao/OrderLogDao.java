package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.OrderLog;

public interface OrderLogDao extends OrderLogDaoCustom, CrudRepository<OrderLog, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface OrderLogDaoCustom extends BaseDao<OrderLog,Long> {
}

