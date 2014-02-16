package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.stereotype.Repository;

import com.hongqiang.shop.common.base.persistence.BaseDaoImpl;
import com.hongqiang.shop.modules.entity.OrderLog;

@Repository
public class OrderLogDaoImpl extends BaseDaoImpl<OrderLog,Long>
  implements OrderLogDaoCustom
{
}
