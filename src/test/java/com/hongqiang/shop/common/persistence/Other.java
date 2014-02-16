package com.hongqiang.shop.common.persistence;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class Other {
	
	
	public static void main(String[] args) {
		Date tDate = new Date();
		System.out.println(tDate);
		tDate = DateUtils.addSeconds(new Date(), 10);
		System.out.println(tDate);
	}
}
