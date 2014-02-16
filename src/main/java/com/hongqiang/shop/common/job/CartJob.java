package com.hongqiang.shop.common.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.account.service.CartService;

@Component("cartJob")
@Lazy(false)
public class CartJob {

	@Autowired
	private CartService cartService;

	@Scheduled(cron = "${job.cart_evict_expired.cron}")
	public void evictExpired() {
		this.cartService.evictExpired();
	}
}