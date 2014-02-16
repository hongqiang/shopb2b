package com.hongqiang.shop.modules.account.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.Cart;

public interface CartDao extends CartDaoCustom, CrudRepository<Cart, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author Jack
 * 
 */
interface CartDaoCustom extends BaseDao<Cart,Long> {

  public void evictExpired();
}