package com.hongqiang.shop.modules.product.dao;

import org.springframework.data.repository.CrudRepository;

import com.hongqiang.shop.common.base.persistence.BaseDao;
import com.hongqiang.shop.modules.entity.Goods;

public abstract interface GoodsDao extends GoodsDaoCustom, CrudRepository<Goods, Long>{
	
}

/**
 * DAO自定义接口
 * @author Jack
 *
 */
interface GoodsDaoCustom extends BaseDao<Goods,Long> {

}