package com.hongqiang.shop.modules.account.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.common.base.persistence.Page;
import com.hongqiang.shop.common.utils.Pageable;
import com.hongqiang.shop.modules.entity.CartItem;

public interface CartItemDao extends CartItemDaoCustom, CrudRepository<CartItem, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface CartItemDaoCustom extends BaseDao<CartItem,Long> {

	public Page<CartItem>  findPage(Pageable pageable);
}