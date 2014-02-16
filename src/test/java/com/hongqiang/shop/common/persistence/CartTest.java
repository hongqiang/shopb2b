package com.hongqiang.shop.common.persistence;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.hongqiang.shop.common.test.SpringTransactionalContextTests;
import com.hongqiang.shop.modules.entity.Order;
import com.hongqiang.shop.modules.shipping.dao.OrderDao;

public class CartTest extends SpringTransactionalContextTests{
	
	@Autowired
	private OrderDao orderDao;
	
	@Test
	public void find(){
		System.out.println("===============================begin==================================");
		Order order = this.orderDao.find(1L);
		System.out.println(order.getAddress()+", "+order.getConsignee()+", "+order.getMemo());
		
		System.out.println("===============================end====================================");
	}
}
