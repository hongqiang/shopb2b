package com.hongqiang.shop.modules.shipping.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.Returns;

public abstract interface ReturnsDao extends ReturnsDaoCustom, CrudRepository<Returns, Long> {
	
}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface ReturnsDaoCustom extends BaseDao<Returns,Long> {

	public Page<Returns>  findPage(Pageable pageable);
}
