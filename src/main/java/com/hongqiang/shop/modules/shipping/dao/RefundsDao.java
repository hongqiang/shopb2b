package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Refunds;

public abstract interface RefundsDao extends RefundsDaoCustom, CrudRepository<Refunds, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface RefundsDaoCustom extends BaseDao<Refunds,Long> {

	public Page<Refunds>  findPage(Pageable pageable);
}