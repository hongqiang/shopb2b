package com.hongqiang.shop.common.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.shipping.service.OrderService;

@Component("orderJob")
@Lazy(false)
public class OrderJob {

	@Autowired
	private OrderService orderService;

	@Scheduled(cron = "${job.order_release_stock.cron}")
	public void releaseStock() {
		this.orderService.releaseStock();
	}
}