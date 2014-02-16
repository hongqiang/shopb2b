package com.hongqiang.shop.common.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hongqiang.shop.modules.util.service.StaticService;

@Component("staticJob")
@Lazy(false)
public class StaticJob {

	@Autowired
	private StaticService staticService;

	@Scheduled(cron = "${job.static_build.cron}")
	public void build() {
		this.staticService.buildAll();
	}
}